package it.polimi.se2019.network.server;

import java.util.Scanner;

public class LaunchTestRmiServer {
    public static void main(String[] args) {
        Connection connection = RmiConnection.create(1111, "connection");
        while (true) {
            new Scanner(System.in).nextLine();
            connection.sendMessage("hello!");
        }
    }
}
