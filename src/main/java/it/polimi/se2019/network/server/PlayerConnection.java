package it.polimi.se2019.network.server;

import java.net.Socket;

public class PlayerConnection {
    //private View mVirtualView;
    private String mIp;
    private boolean mActive;
    // null if rmi, else will be filled in constructor
    private PlayerSocket mPlayerSocket = null;

    public PlayerConnection(Socket socket) {
        mPlayerSocket = new PlayerSocket(socket);
        mIp = socket.getRemoteSocketAddress().toString();
        mActive = true;
    }

    public PlayerConnection(String ip) {
        ip = ip;
        mActive = true;
    }

    /*public View getVirtualView() {
        return mVirtualView;
    }*/

    public String getIp() {
        return mIp;
    }

    public boolean isActive() {
        return mActive;
    }

    public PlayerSocket getPlayerSocket() {
        return mPlayerSocket;
    }

    @Override
    public String toString() {
        return "PlayerConnection{" +
                "mIp='" + mIp + '\'' +
                '}';
    }
}
