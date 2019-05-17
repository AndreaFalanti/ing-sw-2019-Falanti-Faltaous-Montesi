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

public class RegistrationServer implements ConnectionRegister, RegistrationRemote {
    private List<GameThread> mGames;
    private List<PlayerConnection> mWaitingPlayer = new ArrayList<>();
    private List<PlayerConnection> mPlayersOnline = new ArrayList<>();

    private transient Timer mTimer;

    public RegistrationServer(int rmiPort) throws IOException {
        Registry registry = LocateRegistry.createRegistry(rmiPort);
        UnicastRemoteObject.exportObject(this, rmiPort);
        registry.rebind("rmiServer", this);
        System.out.println(">>> rmiServer exported");

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
        System.out.println("\nConnected players:");
        for (PlayerConnection connection : mPlayersOnline) {
            System.out.println(connection.toString());
        }
        System.out.println();
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        if (username == null) {
            return false;
        }
        for (PlayerConnection playerConnection : mPlayersOnline) {
            if (playerConnection.getUsername().equals(username)) {
                System.out.println("Username: " + username + " is already used");
                return false;
            }
        }

        return true;
    }

    @Override
    public void registerConnection(PlayerConnection connection) {
        mWaitingPlayer.add(connection);
        mPlayersOnline.add(connection);

        System.out.println("Player " + connection.getUsername() + " has joined the server");
    }

    @Override
    public void deregisterConnection(PlayerConnection connection) {
        mWaitingPlayer.remove(connection);
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
