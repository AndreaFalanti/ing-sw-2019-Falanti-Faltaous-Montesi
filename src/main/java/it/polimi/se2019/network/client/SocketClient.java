package it.polimi.se2019.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class SocketClient extends Client {
    private DataInputStream mIn;
    private DataOutputStream mOut;
    private Socket mSocket;


    public SocketClient(String serverIp, int serverPort) throws IOException {
        super(serverIp, serverPort);
        mSocket = new Socket(serverIp, serverPort);
        mIn = new DataInputStream(mSocket.getInputStream());
        mOut = new DataOutputStream(mSocket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("Running socket client");
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void receiveMessage() {

    }
}
