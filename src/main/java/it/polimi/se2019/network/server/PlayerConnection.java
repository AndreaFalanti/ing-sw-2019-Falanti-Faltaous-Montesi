package it.polimi.se2019.network.server;

import it.polimi.se2019.view.View;

import java.io.Serializable;

public class PlayerConnection implements Serializable {
    private View mVirtualView;
    private String mUsername;
    private boolean mActive;
    private ConnectionType mType;

    // TODO: add connection type so that server can send message to clients

    public PlayerConnection(String username, ConnectionType type) {
        mUsername = username;
        mActive = true;
        mType = type;
        mVirtualView = null;
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

    public ConnectionType getType() {
        return mType;
    }

    public void setVirtualView(View virtualView) {
        mVirtualView = virtualView;
    }

    @Override
    public String toString() {
        return "PlayerConnection{" +
                "mVirtualView=" + mVirtualView +
                ", mUsername='" + mUsername + '\'' +
                ", mActive=" + mActive +
                ", mType=" + mType +
                '}';
    }
}
