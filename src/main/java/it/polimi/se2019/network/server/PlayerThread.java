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
            mOut = new PrintWriter(socket.getOutputStream());
            mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        mRegister = register;
    }

    public void close(){
        try {
            mSocket.close();
        }
        catch (IOException e) {
            System.err.print("Error during closing");
        }
    }

    @Override
    public void run () {
        String username = null;
        // TODO: read username and check that is valid
        do {
            try {
                username = mIn.readLine();
            }
            // interrupt thread if socket is closed
            catch (IOException e) {
                interrupt();
            }
        } while (!interrupted() && !mRegister.registerPlayer(username));

        if (!interrupted()) {
            PlayerConnection playerConnection = new PlayerConnection(username);
            mRegister.registerConnection(playerConnection);
            mOut.print(true);
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
