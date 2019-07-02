package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.TileColor;

public class AmmoColorSelectedRequest implements Request {
    private TileColor mAmmoColor;
    private PlayerColor mViewColor;

    public AmmoColorSelectedRequest(TileColor ammoColor, PlayerColor viewColor) {
        mAmmoColor = ammoColor;
        mViewColor = viewColor;
    }

    public TileColor getAmmoColor() {
        return mAmmoColor;
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
