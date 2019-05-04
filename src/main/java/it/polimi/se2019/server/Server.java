package it.polimi.se2019.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;

public class Server implements Runnable {

    public static final int PORT = 4567;
    private Socket socket;
    private ServerSocket serverSocket = null;
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<GameThread> games;
    private ArrayList<PlayerOnServer> waitingPlayer = new ArrayList<>();
    // TODO: name this variable
     private ArrayList<PlayerOnServer> playersOnLine = new ArrayList<>();

    public Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
    }

    public void waitingRoom(PlayerOnServer player){
            //TODO implementation
    }

    @Override
    public void run(){
        try {
            // TODO: pass proper argument
            // PlayerOnServer player = new PlayerOnServer();
            Socket socket = serverSocket.accept();
            PlayerOnServer player = new PlayerOnServer(socket,this);
            registerConnection(player);
        //    if(waitingPlayer.size()==1)
      //TODO:          new GameThread(socket)
        } catch(IOException e){
            System.out.println("Errore di connessione");
        }
    }

    public void registerConnection(PlayerOnServer player){
        waitingPlayer.add(player);
    }

    public void deregisterConnection(PlayerOnServer player){
        waitingPlayer.remove(player);
    }

    public static void main(String[] args) {
        Server server;
        try {
            server = new Server();
            server.run();
        } catch (IOException e) {
            System.err.println("Impossibile inizializzare il server");
        }
    }
}