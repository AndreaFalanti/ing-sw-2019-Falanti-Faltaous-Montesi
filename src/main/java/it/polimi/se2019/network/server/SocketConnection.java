package it.polimi.se2019.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnection implements Connection {
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
            e.printStackTrace();
        }
    }

    public static Connection from(Socket socket) {
        return new SocketConnection(socket);
    }

    public static Connection accept(ServerSocket serverSocket) {
        SocketConnection result = null;
        try {
            result = new SocketConnection(serverSocket.accept());
            System.out.println("Accepted client socket");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Connection establish(String host, int port) {
        SocketConnection result = null;
        try {
            result = new SocketConnection(new Socket(host, port));
            System.out.println("Established connection with server [" + host + ", " + port + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void sendMessage(String message) {
        synchronized (mWriteLock) {
            if (mSocket.isClosed())
                throw new IllegalStateException("Socket is closed!");

            System.out.println("sending message: " + message);
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
                e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClosed() {
        return mSocket.isClosed();
    }
}
