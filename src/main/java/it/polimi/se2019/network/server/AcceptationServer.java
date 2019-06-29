package it.polimi.se2019.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptationServer {
    private static final Logger logger = Logger.getLogger(AcceptationServer.class.getName());

    private ServerSocket mServerSocket;
    private RegistrationServer mRegistrationServer;

    public AcceptationServer(int socketPort, RegistrationServer registrationServer) throws IOException {
        mServerSocket = new ServerSocket(socketPort);
        mRegistrationServer = registrationServer;

        logger.log(Level.INFO, "Created server socket on port: {0}", socketPort);
    }


    public void startServer () {
        boolean block = false;
        while (!block) {
            try {
                Socket socket = mServerSocket.accept();
                logger.log(Level.INFO, "Connection accepted: {0}", socket.getRemoteSocketAddress());
                new PlayerRegistrationThread(socket, mRegistrationServer).start();
            } catch (IOException e) {
                logger.severe("Connection error");
                block = true;
            }
        }
    }
}
