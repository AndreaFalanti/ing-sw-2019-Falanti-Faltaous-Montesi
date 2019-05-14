package it.polimi.se2019.network.server;

import java.io.IOException;

public class LaunchServer {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Provide port in cmd");
            return;
        }

        int socketPort = Integer.parseInt(args[0]);
        int rmiPort = Integer.parseInt(args[1]);

        Server server = new Server(socketPort, rmiPort);
        server.start();
    }
}
