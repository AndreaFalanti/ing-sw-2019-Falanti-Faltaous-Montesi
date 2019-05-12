package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;

public class ReloadRequest implements Request {
    private int weaponIndex;

    public ReloadRequest() {
    }

    public int getWeaponIndex() {
        return weaponIndex;
    }

    @Override
    public void handle(RequestHandler handler) {
        handler.handle(this);
    }
}
