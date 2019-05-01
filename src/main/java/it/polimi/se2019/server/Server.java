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

public class Server {

    public static final int PORT = 4567;
    private Socket socket;
    private ServerSocket serverSocket = null;
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<GameThread> games;
    private ArrayList<Socket> clients = new ArrayList<>();
    // TODO: name this variable
    // private ArrayList<PlayerOnServer> = new ArrayList<>();

    public Server() throws IOException {

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Could not listen on this port.");
        }
        while (true) {
            try (Socket socket = serverSocket.accept()){
                // TODO: pass proper argument
                // PlayerOnServer player = new PlayerOnServer();

            }
        }
    }
    // TODO: fix this
    // catch (IOException e) {
      //   e.printStackTrace();
    // }





    public void startServer() {
    }
}