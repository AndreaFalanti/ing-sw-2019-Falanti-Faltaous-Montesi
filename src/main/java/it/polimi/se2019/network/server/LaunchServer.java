package it.polimi.se2019.network.server;

import java.util.logging.Logger;

/**
 * Main class of server jar. Expect socket port and rmi port as command line arguments.
 *
 * @author Andrea Falanti
 */
public class LaunchServer {
    private static final Logger logger = Logger.getLogger(LaunchServer.class.getName());

    public static void main(String[] args) {
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
