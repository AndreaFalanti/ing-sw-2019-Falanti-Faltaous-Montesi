package it.polimi.se2019.network.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Connection that uses sockets
 */
public class SocketConnection implements Connection {
    private static final Logger logger = Logger.getLogger(SocketConnection.class.getName());

    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;

    private boolean mConnected = true;

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

    /**
     * Starts a thread that listens for connections
     * @param port port on which the connections are accepted
     * @param connectionConsumer custom consumer that consumes the accepted connections
     */
    public static void startAccepting(int port, Consumer<Connection> connectionConsumer) {
        new Thread(() -> {
            try (
                    ServerSocket serverSocket = new ServerSocket(port)
            ) {
                while (true) {
                    connectionConsumer.accept(accept(serverSocket));
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }).start();
    }

    /**
     * Wraps a socket in a socket connection
     * @param socket the socket to be wrapped
     * @return the wrapper socket connection
     */
    public static SocketConnection from(Socket socket) {
        return new SocketConnection(socket);
    }

    /**
     * Accepts a connection
     * @param serverSocket the server socket used to accept the connection
     * @return the accepted connection
     */
    private static SocketConnection accept(ServerSocket serverSocket) {
        SocketConnection result = null;
        try {
            result = new SocketConnection(serverSocket.accept());
            logger.info("Accepted client socket");
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return result;
    }

    /**
     * establishes a connection
     * @param serverHost host on which the connection is established
     * @param port the port on which the connection is established
     * @return the established connection
     */
    public static SocketConnection establish(String serverHost, int port) {
        SocketConnection result = null;
        try {
            result = new SocketConnection(new Socket(serverHost, port));
            Object[] logObjects = {serverHost, port};
            logger.log(Level.INFO, "Established connection with server [{0}, {1}]", logObjects);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }

        return result;
    }

    @Override
    public void sendMessage(String message) {
        synchronized (mWriteLock) {
            if (mSocket.isClosed())
                throw new IllegalStateException("Socket is closed!");

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
                mConnected = true;
            } catch (IOException e) {
                if (mConnected) {
                    logger.log(Level.WARNING, "{0} - Player disconnected", e.getMessage());
                    mConnected = false;
                }
            }

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

    @Override
    public String getId() {
        return mSocket.getRemoteSocketAddress().toString();
    }
}
