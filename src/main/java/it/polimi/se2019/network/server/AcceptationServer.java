package it.polimi.se2019.network.server;

import it.polimi.se2019.network.connection.Connection;
import it.polimi.se2019.network.connection.RmiConnection;
import it.polimi.se2019.network.connection.SocketConnection;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptationServer {
    private static final Logger logger = Logger.getLogger(AcceptationServer.class.getName());

    private RegistrationServer mRegistrationServer;

    public AcceptationServer(int socketPort, RegistrationServer registrationServer) {
        mRegistrationServer = registrationServer;

        logger.log(Level.INFO, "Created server socket on port: {0}", socketPort);
    }

    /**
     * Start acceptation server, using a server socket for accepting all entering connections for proper registration
     */
    public void startServer() {
        // initialize rmi
        RmiConnection.init();

        // start accepting rmi and socket connections
        Consumer<Connection> connectionHandler = connection -> {
            logger.log(Level.INFO, "Connection accepted: {0}", connection.getId());
            new PlayerRegistrationThread(connection, mRegistrationServer).start();
        };

        SocketConnection.startAccepting(connectionHandler);
        RmiConnection.startAccepting(connectionHandler);
    }
}

