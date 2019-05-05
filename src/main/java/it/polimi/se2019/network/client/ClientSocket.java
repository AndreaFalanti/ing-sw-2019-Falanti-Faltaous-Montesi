package it.polimi.se2019.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class ClientSocket{

    public static final int PORT = 4567;
    private int port;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;
    private String serverURL = "127.0.0.1";


    public ClientSocket() {
        try {
            socket = new Socket(serverURL,PORT);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error.");
        }
    }

}
