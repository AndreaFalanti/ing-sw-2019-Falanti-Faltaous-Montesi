package it.polimi.se2019.network.server;

import java.io.Serializable;

public class PlayerConnection implements Serializable {
    //private View mVirtualView;
    private String mUsername;
    private boolean mActive;
    // TODO: add connection type so that server can send message to clients

    public PlayerConnection(String username) {
        mUsername = username;
        mActive = true;
    }

    /*public View getVirtualView() {
        return mVirtualView;
    }*/

    public String getUsername() {
        return mUsername;
    }

    public boolean isActive() {
        return mActive;
    }

    @Override
    public String toString() {
        return "PlayerConnection{" +
                "Username='" + mUsername + '\'' +
                '}';
    }
}
