package it.polimi.se2019.network.server;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.InitializationInfo;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.VirtualView;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Thread used to initialize a game room. Start it's creation after game is full of player or after
 * a certain amount of time set in timer
 *
 * @author Andrea Falanti, Stefano Montesi
 */
public class GameThread extends Thread {
    private static final Logger logger = Logger.getLogger(GameThread.class.getName());
    public static final int MAXIMUM_PLAYER = 5;

    private Game mGame;
    private Controller mController;
    private List<PlayerConnection> mPlayerConnections;
    private boolean mStarted = false;

    private Random mRandom = new Random();
    private List<PlayerColor> mColors = new ArrayList<>(Arrays.asList(PlayerColor.values()));

    public GameThread (List<PlayerConnection> players) {
        mPlayerConnections = new ArrayList<>(players);

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

    /**
     * Initialize all client views and start first turn of the game
     */
    @Override
    public void run() {
        // announce start of game thread
        logger.info("The game thread has begun!!");

        for (Map.Entry<PlayerColor, View> entry : mController.getPlayerViews().entrySet()) {
            ((VirtualView)entry.getValue()).startReceivingMessages();
            InitializationInfo initInfo = mGame.extractViewInitializationInfo(entry.getKey());
            entry.getValue().reinitialize(initInfo);
        }

        // start first turn, with proper message flow allow to go to game end.
        mController.handleNextTurn();

        // announce end of game thread
        logger.info("The game thread has ended!");
    }

    /**
     * Create new game, inserting all connected players and initializing their virtual views
     */
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

        int boardID = mRandom.nextInt(4) + 1;
        Board board = Board.fromJson(Jsons.get("boards/game/board" + boardID));
        int killsTarget = 1;

        initializeGame(board, players, killsTarget);
    }

    /**
     * Initialize game with given parameters
     * @param board Selected board
     * @param players Connected players
     * @param killsTarget Kills target of the game
     */
    private void initializeGame (Board board, List<Player> players, int killsTarget) {
        mGame = new Game(board, players, killsTarget);

        mController = new Controller(
                mGame,
                mPlayerConnections.stream()
                        .peek(pc ->
                            pc.setVirtualView(new VirtualView(
                                    pc.getColor(), pc.getConnection()
                            ))
                        )
                        .map(PlayerConnection::getVirtualView)
                        // start sending pings from all virtual views to clients
                        .peek(VirtualView::startCheckingForDisconnection)
                        // collect all views and put them inside controller as normal views
                        .collect(Collectors.toMap(
                                View::getOwnerColor,
                                view -> view
                        ))
        );

        logger.info("Game created successfully");
        start();
    }

    /**
     * Add player to game initialization
     * @param playerConnection Player data
     */
    public void addPlayer (PlayerConnection playerConnection) {
        mPlayerConnections.add(playerConnection);
    }

    /**
     * Remove player from game
     * @param playerConnection Player data
     */
    public void removePlayer (PlayerConnection playerConnection) {
        mPlayerConnections.remove(playerConnection);
    }
}
