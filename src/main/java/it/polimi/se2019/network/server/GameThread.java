package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.InitializationInfo;
import it.polimi.se2019.view.VirtualView;
import it.polimi.se2019.view.View;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.logging.Logger;

public class GameThread extends Thread {
    private static final Logger logger = Logger.getLogger(GameThread.class.getName());
    public static final int MAXIMUM_PLAYER = 5;

    private Game mGame;
    private Controller mController;
    private List<PlayerConnection> mPlayerConnections;
    private boolean mStarted = false;
    private int mRmiPort;

    private Random mRandom = new Random();
    private List<PlayerColor> mColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));

    public GameThread (List<PlayerConnection> players, int rmiPort){
        mPlayerConnections = new ArrayList<>(players);
        mRmiPort = rmiPort;

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mStarted) {
                    startGameCreation();
                }
            }
        },10);
    }

    public int getPlayersNum () {
        return mPlayerConnections.size();
    }

    public boolean isStarted() {
        return mStarted;
    }

    // TODO: change this to handle the whole game
    @Override
    public void run() {
        // announce start of game thread
        logger.info("The game thread has begun!!");

        InitializationInfo initInfo = mGame.extractViewInitializationInfo();
        for (Map.Entry<PlayerColor, View> entry : mController.getPlayerViews().entrySet()) {
            initInfo.setOwnerColor(entry.getKey());
            entry.getValue().reinitialize(initInfo);
        }

        // start first turn, with proper message flow allow to go to game end.
        mController.handleNextTurn();

        // announce end of game thread
        System.out.println("The game thread has ended!");
    }

    public synchronized void startGameCreation () {
        mStarted = true;
        logger.info("Starting game creation ...");
        List<Player> players = new ArrayList<>();

        for (PlayerConnection playerConnection : mPlayerConnections) {
            int randomIndex = mRandom.nextInt(mColors.size());
            PlayerColor color = mColors.get(randomIndex);

            players.add(new Player(playerConnection.getUsername(), color));
            playerConnection.setColor(color);

            mColors.remove(randomIndex);
        }

        Board board = Board.fromJson(Jsons.get("boards/game/board1"));
        int killsTarget = 8;

        initializeGame(board, players, killsTarget);
    }

    private void initializeGame (Board board, List<Player> players, int killsTarget) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(mRmiPort);
        } catch (RemoteException e) {
            throw new IllegalStateException("Could not locate registry for server during game init");
        }

        mGame = new Game(board, players, killsTarget);

        final EnumMap<PlayerColor, View> viewMap = new EnumMap<>(PlayerColor.class);

        mController = new Controller(mGame, viewMap);

        // initialize the view of every player that has connected
        for (PlayerConnection playerConnection : mPlayerConnections) {
            switch (playerConnection.getType()) {
                case SOCKET:
                    playerConnection.setVirtualView(new VirtualView(
                            playerConnection.getColor(), SocketConnection.from(playerConnection.getSocket())
                    ));
                    viewMap.put(playerConnection.getColor(), playerConnection.getVirtualView());
                    break;
                case RMI:
                    playerConnection.setVirtualView(new VirtualView(
                            playerConnection.getColor(), RmiConnection.create(mRmiPort, "connection")
                    ));
                    break;
                default:
                    logger.severe("Invalid connection type");
                    throw new IllegalArgumentException("Invalid connection type");
            }

            // TODO: think about this
            // connect game to virtual view to send updates to client view
            mGame.register(playerConnection.getVirtualView());

            // connect virtual view to controller to receive requests from client and notify them to controller
            playerConnection.getVirtualView().register(mController);

            // start virtual view thread that listens to requests from client view
            playerConnection.getVirtualView().startReceivingRequests();
        }

        logger.info("Game created successfully");
        start();
    }

    public void addPlayer (PlayerConnection playerConnection) {
        mPlayerConnections.add(playerConnection);
    }

    public void removePlayer (PlayerConnection playerConnection) {
        mPlayerConnections.remove(playerConnection);
    }
}
