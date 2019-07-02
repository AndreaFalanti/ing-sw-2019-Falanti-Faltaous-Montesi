package it.polimi.se2019.network.server;

import it.polimi.se2019.network.connection.ConnectionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class PlayerRegistrationThread extends Thread {
    private static final Logger logger = Logger.getLogger(PlayerRegistrationThread.class.getName());

    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private ConnectionRegister mRegister;

    public PlayerRegistrationThread(Socket socket, ConnectionRegister register) {
        mSocket = socket;
        try {
            mOut = new PrintWriter(socket.getOutputStream(), true);
            mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            logger.severe(e.getMessage());
        }

        mRegister = register;
    }

    public void close(){
        try {
            mIn.close();
            mOut.close();
            mSocket.close();
        }
        catch (IOException e) {
           logger.severe("Error during closing");
        }
    }

    /**
     * Receive username
     * @return Received username
     */
    private String receive () {
        try {
            return mIn.readLine();
        }
        catch (IOException e) {
            logger.severe("Critical error while reading from socket (or client closed itself)");
            interrupt();
            return null;
        }
    }

    /**
     * Send a message to client
     * @param message Message to send
     */
    private void send (String message) {
        mOut.println(message);
    }

    /**
     * Handle player registration from socket, it asks for a username and inform client if it's valid or is already
     * picked. If valid, create a PlayerConnection, than close itself.
     */
    @Override
    public void run () {
        String username = null;
        boolean logged = false;

        while (!logged && !isInterrupted()) {
            username = receive();
            if (mRegister.isUsernameAvailable(username)) {
                logged = mRegister.registerPlayer(username, ConnectionType.SOCKET, mSocket);
                send("ok");
            } else {
                send("Username is already used");
            }
        }

        logger.info("Closing registration thread " + getId());
    }

    public Socket getSocket() {
        return mSocket;
    }

    public PrintWriter getOut() {
        return mOut;
    }

    public BufferedReader getIn() {
        return mIn;
    }
}
