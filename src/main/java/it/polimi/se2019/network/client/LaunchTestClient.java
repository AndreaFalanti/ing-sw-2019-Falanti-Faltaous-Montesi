package it.polimi.se2019.network.client;

import it.polimi.se2019.network.server.Connection;
import it.polimi.se2019.network.server.SocketConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static it.polimi.se2019.network.server.LaunchTestSocketServer.SERVER_PORT;

public class LaunchTestClient {
    public static void main(String[] args) {
        Connection connection = SocketConnection.establish("localhost", SERVER_PORT);
        while (true) {
            String message = connection.waitForMessage();
            System.out.println("Message from server: " + message);
        }
    }

    public static void main0(String[] args) {
        try (
                Socket socket = new Socket("localhost", SERVER_PORT);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            while (true) {
                String messsage = in.readLine();
                System.out.printf("Received message: " + messsage);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
