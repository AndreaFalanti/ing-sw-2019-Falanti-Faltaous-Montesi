package it.polimi.se2019.network.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationServer implements ConnectionRegister, RegistrationRemote {
    private static final Logger logger = Logger.getLogger(RegistrationServer.class.getName());
    
    private List<GameThread> mGames = new ArrayList<>();
    private List<PlayerConnection> mWaitingPlayers = new ArrayList<>();
    private List<PlayerConnection> mPlayersOnline = new ArrayList<>();

    private transient Timer mTimer;

    public RegistrationServer(int rmiPort) throws IOException {
        Registry registry = LocateRegistry.createRegistry(rmiPort);
        UnicastRemoteObject.exportObject(this, rmiPort);
        registry.rebind("rmiServer", this);
        logger.log(Level.INFO, "RMI initialized on port {0}\nexported modules:\nrmiServer", rmiPort);

        // print every 10 seconds the list of players connected
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                printConnectedPlayers();
            }
        },10000, 10000);
    }

    private void printConnectedPlayers () {
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

    @Override
    public boolean isUsernameAvailable(String username) {
        if (username == null) {
            return false;
        }
        for (PlayerConnection playerConnection : mPlayersOnline) {
            if (playerConnection.getUsername().equals(username)) {
                logger.log(Level.WARNING, "Username: {0} is already used", username);
                return false;
            }
        }

        return true;
    }

    @Override
    public synchronized void registerConnection(PlayerConnection connection) {
        mWaitingPlayers.add(connection);
        mPlayersOnline.add(connection);

        logger.log(Level.INFO, "Player {0} has joined the server", connection.getUsername());

        if (mWaitingPlayers.size() >= 3) {
            GameThread gameThread = new GameThread(mWaitingPlayers);
            mGames.add(gameThread);
            gameThread.start();

            mWaitingPlayers.clear();
        }
        else if (!mGames.isEmpty()) {
            for (GameThread game : mGames) {
                if (!game.isStarted() && game.getPlayersNum() < 5) {
                    game.addPlayer(connection);
                    return;
                }
            }
        }
    }

    @Override
    public void deregisterConnection(PlayerConnection connection) {
        mWaitingPlayers.remove(connection);
        mPlayersOnline.remove(connection);
    }

    @Override
    public boolean registerPlayer(String username) {
        if (isUsernameAvailable(username)) {
            PlayerConnection connection = new PlayerConnection(username);
            registerConnection(connection);
            return true;
        }

        return false;
    }

    @Override
    public void deregisterPlayer(String username) {
        for (PlayerConnection connection : mPlayersOnline) {
            if (connection.getUsername().equals(username)) {
                deregisterConnection(connection);
            }
        }
    }

    @Override
    public boolean registerPlayerRemote(String username) throws RemoteException {
        return registerPlayer(username);
    }

    @Override
    public void deregisterPlayerRemote(String username) throws RemoteException {
        deregisterPlayer(username);
    }
}
