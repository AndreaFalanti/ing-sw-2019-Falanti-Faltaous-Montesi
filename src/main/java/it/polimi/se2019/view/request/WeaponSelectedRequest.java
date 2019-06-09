package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

public class WeaponSelectedRequest implements Request {
    private int mWeaponIndex;
    private View mView;

    public WeaponSelectedRequest(int weaponIndex, View view) {
        mWeaponIndex = weaponIndex;
        mView = view;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
