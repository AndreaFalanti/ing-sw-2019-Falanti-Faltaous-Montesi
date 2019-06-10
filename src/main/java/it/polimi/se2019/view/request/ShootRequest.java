package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;

public class ShootRequest implements Request {
    private View mView;
    private String mWeaponID;
    private PlayerColor mShooterColor;

    public ShootRequest(View view, String weaponID, PlayerColor shooterColor) {
        mView = view;
        mWeaponID = weaponID;
        mShooterColor = shooterColor;
    }

    public View getView() {
        return mView;
    }

    public String getWeaponID() {
        return mWeaponID;
    }

    public PlayerColor getShooterColor() {
        return mShooterColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
