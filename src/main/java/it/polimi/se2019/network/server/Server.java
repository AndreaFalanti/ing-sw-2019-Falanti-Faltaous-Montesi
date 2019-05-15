package it.polimi.se2019.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Server implements RmiServerRemote {
    private List<GameThread> mGames;
    private List<PlayerConnection> mWaitingPlayer = new ArrayList<>();
    private List<PlayerConnection> mPlayersOnline = new ArrayList<>();

    private Socket mSocket;
    private transient ServerSocket mServerSocket = null;
    private DataInputStream mIn;
    private DataOutputStream mOut;

    private transient Timer mTimer;

    public Server(int socketPort, int rmiPort) throws IOException {
        mServerSocket = new ServerSocket(socketPort);

        Registry registry = LocateRegistry.createRegistry(rmiPort);
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

    public void waitingRoom(PlayerConnection player){
    }

    public void registerConnection(PlayerConnection player){
        mWaitingPlayer.add(player);
        mPlayersOnline.add(player);
    }

    public void deregisterConnection(PlayerConnection player){
        mWaitingPlayer.remove(player);
        mPlayersOnline.remove(player);
    }

    @Override
    public void registerConnection() throws RemoteException {
        String clientIp;
        try {
            clientIp = RemoteServer.getClientHost();
            PlayerConnection connection = new PlayerConnection(clientIp);
            registerConnection(connection);
        }
        catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deregisterConnection() throws RemoteException {
        try {
            String clientIp = RemoteServer.getClientHost();
            for (PlayerConnection connection : mPlayersOnline) {
                if (connection.getIp().equals(clientIp)) {
                    deregisterConnection(connection);
                }
            }
        }
        catch (ServerNotActiveException e) {
            System.out.println("Error occurred while deregistering rmi player");
        }
    }

    private void printConnectedPlayers () {
        System.out.println("\nConnected players:");
        for (PlayerConnection connection : mPlayersOnline) {
            System.out.println(connection.toString());
        }
    }

    public void start(){
        try {
            Socket socket = mServerSocket.accept();
            PlayerConnection playerConnection = new PlayerConnection(socket);
            System.out.println("Connection accepted: " + socket.getRemoteSocketAddress());
            registerConnection(playerConnection);
            //TODO: add multiple game threads when ready
        } catch(IOException e){
            System.out.println("Connection error");
        }
    }
}
