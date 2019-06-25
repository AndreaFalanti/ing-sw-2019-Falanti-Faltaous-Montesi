package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class PositionSelectedRequest implements Request {
    private Position mPosition;
    private PlayerColor mViewColor;

    public PositionSelectedRequest(Position position, PlayerColor viewColor) {
        mPosition = position;
        mViewColor = viewColor;
    }

    public Position getPosition() {
        return mPosition;
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
