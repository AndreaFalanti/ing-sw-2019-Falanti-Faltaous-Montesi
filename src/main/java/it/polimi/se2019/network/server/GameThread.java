package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.RmiVirtualView;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.ViewFactory;

import java.util.*;
import java.util.logging.Logger;

public class GameThread extends Thread {
    private static final Logger logger = Logger.getLogger(GameThread.class.getName());
    public static final int MAXIMUM_PLAYER = 5;

    private Game mGame;
    private Controller mController;
    private View[] mVirtualViews;
    private List<PlayerConnection> mPlayerConnections;
    private boolean mStarted = false;

    private Random mRandom = new Random();
    private List<PlayerColor> mColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));

    public GameThread (List<PlayerConnection> players){
        mPlayerConnections = new ArrayList<>(players);

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mStarted) {
                    startGameCreation();
                }
            }
        },20000);
    }

    public int getPlayersNum () {
        return mPlayerConnections.size();
    }

    public boolean isStarted() {
        return mStarted;
    }

    @Override
    public void run() {
        logger.info("Game is started");
    }

    public synchronized void startGameCreation () {
        mStarted = true;
        logger.info("Starting game creation ...");
        List<Player> players = new ArrayList<>();

        // TODO: make players choose colors and board?
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
        mGame = new Game(board, players, killsTarget);

        EnumMap<PlayerColor, View> viewMap = new EnumMap<>(PlayerColor.class);
        for (PlayerConnection playerConnection : mPlayerConnections) {
            switch (playerConnection.getType()) {
                case SOCKET:
                    playerConnection.setVirtualView(ViewFactory.createSocketVirtualView(
                            playerConnection.getSocket(), playerConnection.getColor()));
                    break;
                case RMI:
                    playerConnection.setVirtualView(new RmiVirtualView());
                    break;
                default:
                    logger.severe("Invalid connection type");
                    throw new IllegalArgumentException("Invalid connection type");
            }

            viewMap.put(playerConnection.getColor(), playerConnection.getVirtualView());
        }

        mController = new Controller(mGame, viewMap);

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
