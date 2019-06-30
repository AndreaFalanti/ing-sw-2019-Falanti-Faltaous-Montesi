package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

public class UsePowerUpRequest implements Request {
    private int mPowerUpIndex;
    private PlayerColor mViewColor;

    public UsePowerUpRequest(int powerUpIndex, PlayerColor viewColor) {
        mPowerUpIndex = powerUpIndex;
        mViewColor = viewColor;
    }

    public int getPowerUpIndex() {
        return mPowerUpIndex;
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
