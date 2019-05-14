package it.polimi.se2019.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Server implements RmiServerRemote {
    private List<GameThread> mGames;
    private List<PlayerConnection> mWaitingPlayer = new ArrayList<>();
    private List<PlayerConnection> mPlayersOnline = new ArrayList<>();

    private Socket mSocket;
    private ServerSocket mServerSocket = null;
    private DataInputStream mIn;
    private DataOutputStream mOut;

    public Server(int socketPort, int rmiPort) throws IOException {
        mServerSocket = new ServerSocket(socketPort);

        RmiServerRemote rmiServerRemote = this;
        System.out.println(">>> rmiServer exported");

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("rmiServer", rmiServerRemote);
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

    }

    @Override
    public void deregisterConnection() throws RemoteException {

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
