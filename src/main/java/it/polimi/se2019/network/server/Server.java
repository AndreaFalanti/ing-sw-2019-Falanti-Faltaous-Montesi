package it.polimi.se2019.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private List<GameThread> games;
    private List<PlayerConnection> waitingPlayer = new ArrayList<>();
    private List<PlayerConnection> playersOnline = new ArrayList<>();

    private Socket socket;
    private ServerSocket serverSocket = null;
    private DataInputStream in;
    private DataOutputStream out;
    public static final int SOCKET_PORT = 4567;

    public static final int RMI_PORT = 8000;

    public Server() throws IOException {
        serverSocket = new ServerSocket(SOCKET_PORT);
    }

    public void waitingRoom(PlayerConnection player){
    }
    public void registerConnection(PlayerConnection player){
        waitingPlayer.add(player);
    }

    public void deregisterConnection(PlayerConnection player){
        waitingPlayer.remove(player);
    }

    @Override
    public void run(){
        try {
            // TODO: pass proper argument
            // SocketPlayerConnection player = new SocketPlayerConnection();
            Socket socket = serverSocket.accept();
            SocketPlayerConnection player = new SocketPlayerConnection(socket,this);
            registerConnection(player);
            //    if(waitingPlayer.size()==1)
            //TODO:          new GameThread(socket)
        } catch(IOException e){
            System.out.println("Connection error");
        }
    }

    public static void main(String[] args) {
        Server server;
        try {
            server = new Server();
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize network");
        }
    }

}
