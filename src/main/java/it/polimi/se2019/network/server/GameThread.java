package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.controller.weapon.Weapons;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewFactory;

import java.rmi.NotBoundException;
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
        System.out.println("The game thread has begun!!");

        mGame.getPlayers().stream().filter(pl -> pl.getName().equals("Mario")).findAny().get()
                .move(new Position(3, 2));
        mGame.getPlayers().stream().filter(pl -> pl.getName().equals("Luigi")).findAny().get()
                .move(new Position(2, 0));
        mGame.getPlayers().stream().filter(pl -> pl.getName().equals("Smurfette")).findAny().get()
                .move(new Position(2, 0));

        // start random shoot interaction
        mController.startShootInteraction(
                mPlayerConnections.stream()
                        .filter(pc -> pc.getUsername().equals("Mario"))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Mario is needed to test server..."))
                        .getColor(),
                Weapons.get("heatseeker").getBehaviour()
        );

        // wait for shoot interaction to be done
        ShootInteraction interaction = mController.getShootInteraction();
        while (interaction.isOccupied()) {
            synchronized (interaction.getLock()) {
                try {
                    interaction.getLock().wait();
                } catch (InterruptedException e) {
                    System.out.println("Test thread interrupted while waiting for shoot interaction to finish...");
                    Thread.currentThread().interrupt();
                }
            }
        }

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

        for (PlayerConnection playerConnection : mPlayerConnections) {
            switch (playerConnection.getType()) {
                case SOCKET:
                    playerConnection.setVirtualView(ViewFactory.createSocketVirtualView(
                            playerConnection.getSocket(), playerConnection.getColor()));
                    viewMap.put(playerConnection.getColor(), playerConnection.getVirtualView());
                    break;
                case RMI:
                    // TODO: make player select view type

                    View stub = null;
                    String viewColor = playerConnection.getColor().getPascalName();
                    try {
                        stub = (View) registry.lookup(viewColor);
                    } catch (RemoteException | NotBoundException e) {
                        throw new IllegalStateException("exception while performing lookup of "
                                + viewColor + " view on server");
                    }
                    viewMap.put(playerConnection.getColor(), stub);
                    playerConnection.setVirtualView(stub);
                    break;
                default:
                    logger.severe("Invalid connection type");
                    throw new IllegalArgumentException("Invalid connection type");
            }

            mGame.register(playerConnection.getVirtualView());
            playerConnection.getVirtualView().register(mController);
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
