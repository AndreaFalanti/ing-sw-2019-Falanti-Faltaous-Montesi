package it.polimi.se2019.network.client;

import it.polimi.se2019.network.server.Connection;
import it.polimi.se2019.network.server.RmiConnection;

public class LaunchRmiTestClient {
    public static void main(String[] args) {
        Connection connection = RmiConnection.establish(1111, "connection");
        while (true) {
            String message = connection.waitForMessage();
            System.out.println("Received message: " + message);
        }
    }
}
