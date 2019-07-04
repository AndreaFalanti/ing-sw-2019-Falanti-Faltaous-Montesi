package it.polimi.se2019.network.server;

import java.io.IOException;
import java.util.logging.Logger;

public class LaunchServer {
    private static final Logger logger = Logger.getLogger(LaunchServer.class.getName());

    public static final String RMI_SERVER_ID = "rmiServer";

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            logger.warning("Provide ports in cmd");
            return;
        }

        int socketPort = Integer.parseInt(args[0]);
        int rmiPort = Integer.parseInt(args[1]);

        RegistrationServer registrationServer = new RegistrationServer();
        AcceptationServer acceptationServer = new AcceptationServer(socketPort, registrationServer);

        acceptationServer.startServer();
    }
}
