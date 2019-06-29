package it.polimi.se2019.network.server;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;

import java.net.Socket;

public class PlayerConnection {
    private View mVirtualView;
    private String mUsername;
    private boolean mActive;
    private PlayerColor mColor;
    private ConnectionType mType;

    private Socket mSocket;

    // TODO: add connection type so that server can send message to clients

    public PlayerConnection(String username, ConnectionType type) {
        mUsername = username;
        mActive = true;
        mType = type;
        mVirtualView = null;

        mSocket = null;
    }

    public PlayerConnection(String username, ConnectionType type, Socket socket) {
        this (username, type);
        mSocket = socket;
    }

    public View getVirtualView() {
        return mVirtualView;
    }

    public String getUsername() {
        return mUsername;
    }

    public boolean isActive() {
        return mActive;
    }

    public PlayerColor getColor() {
        return mColor;
    }

    public ConnectionType getType() {
        return mType;
    }

    public Socket getSocket() {
        return mSocket;
    }

    public void setVirtualView(View virtualView) {
        mVirtualView = virtualView;
    }

    public void setColor(PlayerColor color) {
        mColor = color;
    }

    @Override
    public String toString() {
        return "PlayerConnection{" +
                "mVirtualView=" + mVirtualView +
                ", mUsername='" + mUsername + '\'' +
                ", mActive=" + mActive +
                ", mColor=" + mColor +
                ", mType=" + mType +
                '}';
    }
}
