package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

public class WeaponSelectedRequest implements Request {
    private int mWeaponIndex;
    private PlayerColor mViewColor;

    public WeaponSelectedRequest(int weaponIndex, PlayerColor viewColor) {
        mWeaponIndex = weaponIndex;
        mViewColor = viewColor;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    @Override
    public PlayerColor getViewColor() {
        return mViewColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
