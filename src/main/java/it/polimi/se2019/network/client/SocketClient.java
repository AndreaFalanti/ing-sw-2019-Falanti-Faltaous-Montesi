package it.polimi.se2019.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class SocketClient extends Client {
    private BufferedReader mIn;
    private PrintWriter mOut;
    private Socket mSocket;

    public SocketClient(String serverIp, int serverPort) throws IOException {
        super(serverIp, serverPort);
        mSocket = new Socket(serverIp, serverPort);
        mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        mOut = new PrintWriter(mSocket.getOutputStream(), true);
    }

    private static void printLineToConsole(String message) {
        System.out.println(message);
    }

    private static void printToConsole(String message) {
        System.out.print(message);
    }

    private String receive () {
        try {
            return mIn.readLine();
        } catch (IOException e) {
            printLineToConsole("Critical error while reading from socket");
            return null;
        }
    }

    private void send (String message) {
        mOut.println(message);
    }

    @Override
    public void run() {
        printLineToConsole("Running socket client");
        Scanner scanner = new Scanner(System.in);
        String message;
        boolean validUsername;

        printLineToConsole("Insert username: ");

        do {
            printToConsole(">>");
            mOut.println(scanner.nextLine());
            message = receive();
            if (message == null || !message.equals("ok")) {
                validUsername = false;
                printLineToConsole(message);
            }
            else {
                validUsername = true;
                printLineToConsole("Username is valid");
            }
        } while (!validUsername);
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public String receiveMessage() {
        return null;
    }
}
