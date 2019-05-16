package it.polimi.se2019.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptationServer {
    private transient ServerSocket mServerSocket;
    private RegistrationServer mRegistrationServer;

    public AcceptationServer(int socketPort, RegistrationServer registrationServer) throws IOException {
        mServerSocket = new ServerSocket(socketPort);
        mRegistrationServer = registrationServer;
    }


    public void startServer () {
        while (true) {
            try {
                Socket socket = mServerSocket.accept();
                PlayerThread playerThread = new PlayerThread(socket, mRegistrationServer);
                System.out.println("Connection accepted: " + socket.getRemoteSocketAddress());
                playerThread.start();
            } catch (IOException e) {
                System.out.println("Connection error");
            }
        }
    }
}
