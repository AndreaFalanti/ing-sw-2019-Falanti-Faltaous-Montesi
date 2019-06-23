package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.view.View;

public class DirectionSelectedRequest implements Request {
    private Direction mDirection;
    private View mView;

    public DirectionSelectedRequest(Direction direction, View view) {
        mDirection = direction;
        mView = view;
    }

    public Direction getDirection() {
        return mDirection;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
