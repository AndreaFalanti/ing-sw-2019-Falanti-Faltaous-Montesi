package it.polimi.se2019.network.server;

import it.polimi.se2019.view.View;

public abstract class PlayerConnection {
    private View virtualView;
    private boolean active;

    public boolean isActive() {return active;}
}
