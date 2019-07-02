package it.polimi.se2019.network.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketConnection implements Connection {
    private static final Logger logger = Logger.getLogger(SocketConnection.class.getName());

    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;

    private final Object mReadLock = new Object();
    private final Object mWriteLock = new Object();

    private SocketConnection(Socket socket) {
        mSocket = socket;

        // initialize socket streams
        try {
            mOut = new PrintWriter(mSocket.getOutputStream(), true);
            mIn = new BufferedReader(
                    new InputStreamReader(mSocket.getInputStream())
            );
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }
    }

    public static Connection from(Socket socket) {
        return new SocketConnection(socket);
    }

    public static Connection accept(ServerSocket serverSocket) {
        SocketConnection result = null;
        try {
            result = new SocketConnection(serverSocket.accept());
            logger.info("Accepted client socket");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }

        return result;
    }

    public static Connection establish(String host, int port) {
        SocketConnection result = null;
        try {
            result = new SocketConnection(new Socket(host, port));
            Object[] logObjects = {host, port};
            logger.log(Level.INFO, "Established connection with server [{0}, {1}]", logObjects);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }

        return result;
    }

    @Override
    public void sendMessage(String message) {
        synchronized (mWriteLock) {
            if (mSocket.isClosed())
                throw new IllegalStateException("Socket is closed!");

            // TODO: block with filter
            // logger.log(Level.INFO, "sending message: {0}", message);

            mOut.println(message);
            mOut.flush();
        }
    }

    @Override
    public String waitForMessage() {
        synchronized (mReadLock) {
            if (mSocket.isClosed())
                throw new IllegalStateException("Socket is closed!");

            String result = null;
            try {
                result = mIn.readLine();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
            }

            // if result is null, the other connection endpoint has disconnected abruptly
            if (result == null) {
                // TODO: handle this gracefully
                throw new UnsupportedOperationException("WIP");
            }

            // TODO: block this with filter
            // logger.log(Level.INFO, "Received message: {0}", result);

            return result;
        }
    }

    @Override
    public void close() {
        try {
            mSocket.close();
            mIn.close();
            mOut.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override
    public boolean isClosed() {
        return mSocket.isClosed();
    }
}
