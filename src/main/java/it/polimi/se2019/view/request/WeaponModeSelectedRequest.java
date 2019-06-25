package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

public class WeaponModeSelectedRequest implements Request {
    private String mId;
    private PlayerColor mViewColor;

    public WeaponModeSelectedRequest(String id, PlayerColor viewColor) {
        mId = id;
        mViewColor = viewColor;
    }

    public String getId() {
        return mId;
    }

    public PlayerColor getViewColor() {
        return mViewColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
