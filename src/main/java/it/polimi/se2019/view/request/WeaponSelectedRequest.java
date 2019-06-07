package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;

public class WeaponSelectedRequest implements Request {
    private int mWeaponIndex;

    public WeaponSelectedRequest(int weaponIndex) {
        mWeaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
