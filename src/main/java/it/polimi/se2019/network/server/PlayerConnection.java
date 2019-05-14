package it.polimi.se2019.network.server;

import java.net.Socket;
import java.net.SocketAddress;

public class PlayerConnection {
    //private View mVirtualView;
    private SocketAddress ip;
    private boolean mActive;
    // null if rmi, else will be filled in constructor
    private PlayerSocket mPlayerSocket = null;

    public PlayerConnection(Socket socket) {
        mPlayerSocket = new PlayerSocket(socket);
        ip = socket.getRemoteSocketAddress();
        mActive = true;
    }

    /*public View getVirtualView() {
        return mVirtualView;
    }*/

    public SocketAddress getIp() {
        return ip;
    }

    public boolean isActive() {
        return mActive;
    }

    public PlayerSocket getPlayerSocket() {
        return mPlayerSocket;
    }
}
