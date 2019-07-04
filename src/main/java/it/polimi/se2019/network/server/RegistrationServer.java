package it.polimi.se2019.network.server;

import it.polimi.se2019.network.connection.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationServer implements ConnectionRegister {
    private static final Logger logger = Logger.getLogger(RegistrationServer.class.getName());

    private List<GameThread> mGames = new ArrayList<>();
    private List<PlayerConnection> mWaitingPlayers = new ArrayList<>();
    private List<PlayerConnection> mPlayersOnline = new ArrayList<>();

    public RegistrationServer() {
        /*
        // print every 10 seconds the list of players connected
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                printConnectedPlayers();
            }
        },10000, 10000);
        */
    }

    /**
     * Print all connected players on the server and evidence players still in waiting queue.
     */
    private void printConnectedPlayers() {
        StringBuilder log = new StringBuilder("Connected players:");
        for (PlayerConnection connection : mPlayersOnline) {
            log.append("\n" + connection.toString());
        }

        log.append("\nWaiting players:");
        for (PlayerConnection connection : mWaitingPlayers) {
            log.append("\n" + connection.toString());
        }

        logger.info(log.toString());
    }

    /**
     * Check if username is not already picked
     *
     * @param username Username to check
     * @return True if available, false otherwise
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        if (username == null) {
            return false;
        }
        for (PlayerConnection playerConnection : mPlayersOnline) {
            if (playerConnection.getUsername().equalsIgnoreCase(username)) {
                logger.log(Level.WARNING, "Username: {0} is already used", username);
                return false;
            }
        }

        return true;
    }

    /**
     * Register a player connection to proper queues. If a starting game is not full of players, add this new player
     * to it instead of adding it to waiting queue.
     *
     * @param connection Connection to register
     */
    @Override
    public synchronized void registerConnection(PlayerConnection connection) {
        mWaitingPlayers.add(connection);
        mPlayersOnline.add(connection);

        logger.log(Level.INFO, "Player {0} has joined the server", connection.getUsername());

        // if 3 or more players are connected, initialize game values and start timer for game creation
        if (mWaitingPlayers.size() >= 3) {
            GameThread gameThread = new GameThread(mWaitingPlayers);
            mGames.add(gameThread);

            mWaitingPlayers.clear();
        }
        // if not, check if there are any game not started with less then five players
        // and add player directly to it
        else if (!mGames.isEmpty()) {
            for (GameThread game : mGames) {
                if (!game.isStarted() && game.getPlayersNum() < GameThread.MAXIMUM_PLAYER) {
                    game.addPlayer(connection);
                    mWaitingPlayers.remove(connection);

                    // if game is full, start the game
                    if (game.getPlayersNum() == GameThread.MAXIMUM_PLAYER) {
                        game.startGameCreation();
                    }
                    return;
                }
            }
        }
    }

    /**
     * Deregister a player connection from the server queues
     *
     * @param connection PlayerConnection to deregister
     */
    @Override
    public void deregisterConnection(PlayerConnection connection) {
        mWaitingPlayers.remove(connection);
        mPlayersOnline.remove(connection);
    }

    /**
     * Register a player to the server queues if username is valid
     *
     * @param username Player's username
     * @return true if successfully registered, false if not
     */
    @Override
    public boolean registerPlayer(String username, Connection connection) {
        if (isUsernameAvailable(username)) {
            PlayerConnection playerConnection = new PlayerConnection(username, connection);
            registerConnection(playerConnection);
            return true;
        }

        return false;
    }

    /**
     * Deregister a player from the server
     *
     * @param username Player's username
     */
    @Override
    public void deregisterPlayer(String username) {
        for (PlayerConnection connection : mPlayersOnline) {
            if (connection.getUsername().equals(username)) {
                deregisterConnection(connection);
            }
        }
    }
}
