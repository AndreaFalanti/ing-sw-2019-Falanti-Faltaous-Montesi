package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.Direction;

public class DirectionSelectedRequest implements Request {
    private Direction mDirection;
    private PlayerColor mViewColor;

    public DirectionSelectedRequest(Direction direction, PlayerColor viewColor) {
        mDirection = direction;
        mViewColor = viewColor;
    }

    public Direction getDirection() {
        return mDirection;
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
