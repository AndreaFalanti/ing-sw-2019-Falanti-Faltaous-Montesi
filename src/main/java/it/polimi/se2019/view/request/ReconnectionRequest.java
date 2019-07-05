package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

public class ReconnectionRequest implements Request {
    private PlayerColor mPlayerColor;

    public ReconnectionRequest(PlayerColor playerColor) {
        mPlayerColor = playerColor;
    }

    @Override
    public PlayerColor getViewColor() {
        return mPlayerColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}

