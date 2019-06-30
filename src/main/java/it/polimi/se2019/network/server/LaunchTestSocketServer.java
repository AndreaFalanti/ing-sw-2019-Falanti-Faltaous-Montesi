package it.polimi.se2019.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LaunchTestSocketServer {
    public static final int SERVER_PORT = 4567;

    public static void main(String[] args) {
        try (
                ServerSocket serverSocket = new ServerSocket(SERVER_PORT)
        ) {
            Connection connection = SocketConnection.accept(serverSocket);

            while (true) {
                new Scanner(System.in).nextLine();
                connection.sendMessage("hello!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

