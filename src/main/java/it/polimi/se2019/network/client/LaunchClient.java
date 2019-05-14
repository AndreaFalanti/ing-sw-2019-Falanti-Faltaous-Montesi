package it.polimi.se2019.network.client;

import java.io.IOException;
import java.util.Scanner;

public class LaunchClient {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Provide host:port please");
            return;
        }

        String[] tokens = args[0].split(":");

        if (tokens.length < 2) {
            throw new IllegalArgumentException("Bad formatting: " + args[0]);
        }

        String host = tokens[0];
        int port = Integer.parseInt(tokens[1]);

        Scanner fromKeyboard = new Scanner(System.in);

        // TODO: choose client type: socket or rmi
        // TODO: choose type of view

        //Client client = new Client(host, port);
    }
}
