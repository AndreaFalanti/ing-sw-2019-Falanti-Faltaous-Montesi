package it.polimi.se2019.network.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class SocketClient extends Client {
    private DataInputStream mIn;
    private PrintWriter mOut;
    private Socket mSocket;


    public SocketClient(String serverIp, int serverPort) throws IOException {
        super(serverIp, serverPort);
        mSocket = new Socket(serverIp, serverPort);
        mIn = new DataInputStream(mSocket.getInputStream());
        mOut = new PrintWriter(mSocket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("Running socket client");
        Scanner scanner = new Scanner(System.in);
        String username;
        Boolean validUsername;

        do {
            System.out.println("Choose a username: ");
            System.out.print(">> ");
            mOut.println(scanner.nextLine());
            try {
                validUsername = mIn.readBoolean();
            } catch (IOException e) {
                e.printStackTrace();
                validUsername = false;
            }
        } while (!validUsername);
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void receiveMessage() {

    }
}
