package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

public class ShootRequest implements Request {
    private String mWeaponID;
    private PlayerColor mShooterColor;
    private PlayerColor mViewColor;

    public ShootRequest(PlayerColor viewColor, String weaponID, PlayerColor shooterColor) {
        mViewColor = viewColor;
        mWeaponID = weaponID;
        mShooterColor = shooterColor;
    }

    @Override
    public PlayerColor getViewColor() {
        return mViewColor;
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
