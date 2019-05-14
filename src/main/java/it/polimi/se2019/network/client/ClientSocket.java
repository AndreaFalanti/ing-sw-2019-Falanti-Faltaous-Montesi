package it.polimi.se2019.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;



public class ClientSocket extends Client {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;


    public ClientSocket(String serverIp, int serverPort) {
        super(serverIp, serverPort);
    }
}
