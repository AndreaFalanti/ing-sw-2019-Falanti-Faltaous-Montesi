package it.polimi.se2019.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class AcceptationServer {
    private static final Logger logger = Logger.getLogger(AcceptationServer.class.getName());

    private transient ServerSocket mServerSocket;
    private RegistrationServer mRegistrationServer;

    public AcceptationServer(int socketPort, RegistrationServer registrationServer) throws IOException {
        mServerSocket = new ServerSocket(socketPort);
        mRegistrationServer = registrationServer;

        logger.info("Created server socket on port: " + socketPort);
    }


    public void startServer () {
        while (true) {
            try {
                Socket socket = mServerSocket.accept();
                logger.info("Connection accepted: " + socket.getRemoteSocketAddress());
                new PlayerThread(socket, mRegistrationServer).start();
            } catch (IOException e) {
                logger.severe("Connection error");
            }
        }
    }
}
