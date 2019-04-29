package it.polimi.se2019.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private int port;
    private ServerSocket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ArrayList<GameThread> games;


    public Server(int port) {
        this.port = port;
    }


    public void startServer() {
    }
}