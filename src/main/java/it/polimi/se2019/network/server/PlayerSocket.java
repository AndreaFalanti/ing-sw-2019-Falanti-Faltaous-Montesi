package it.polimi.se2019.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerSocket {
    private Socket mSocket;
    private DataOutputStream mOut;
    private DataInputStream mIn;

    public PlayerSocket (Socket socket) {
        mSocket = socket;
        try {
            mOut = new DataOutputStream(socket.getOutputStream());
            mIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            mSocket.close();
        }
        catch (IOException e) {
            System.err.print("Error during closing");
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    public DataOutputStream getOut() {
        return mOut;
    }

    public DataInputStream getIn() {
        return mIn;
    }
}
