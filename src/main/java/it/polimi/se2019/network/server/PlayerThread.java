package it.polimi.se2019.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerThread extends Thread {
    private Socket mSocket;
    private PrintWriter mOut;
    private BufferedReader mIn;

    private ConnectionRegister mRegister;

    public PlayerThread(Socket socket, ConnectionRegister register) {
        mSocket = socket;
        try {
            mOut = new PrintWriter(socket.getOutputStream(), true);
            mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
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
            System.err.print("Error during closing");
        }
    }

    private String receive () {
        try {
            return mIn.readLine();
        } catch (IOException e) {
            System.out.println("Critical error while reading from socket (or client closed itself)");
            interrupt();
            return null;
        }
    }

    private void send (String message) {
        mOut.println(message);
    }

    @Override
    public void run () {
        String username = null;
        boolean logged = false;

        while (!interrupted() && !logged) {
            username = receive();
            if (mRegister.isUsernameAvailable(username)) {
                logged = mRegister.registerPlayer(username);
                send("ok");
            }
            else {
                send("Username is already used");
            }
        }
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
