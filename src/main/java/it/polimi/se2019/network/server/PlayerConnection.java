package it.polimi.se2019.network.server;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.network.connection.Connection;
import it.polimi.se2019.network.connection.ConnectionType;
import it.polimi.se2019.view.VirtualView;

import java.net.Socket;

public class PlayerConnection {
    private VirtualView mVirtualView;
    private String mUsername;
    private boolean mActive;
    private PlayerColor mColor;

    private Connection mConnection;

    public PlayerConnection(String username, Connection connection) {
        mUsername = username;
        mConnection = connection;
        mActive = true;
        mVirtualView = null;
    }


    public VirtualView getVirtualView() {
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

    public Connection getConnection() {
        return mConnection;
    }

    public void setVirtualView(VirtualView virtualView) {
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
                ", mConnection=" + mConnection +
                '}';
    }
}
