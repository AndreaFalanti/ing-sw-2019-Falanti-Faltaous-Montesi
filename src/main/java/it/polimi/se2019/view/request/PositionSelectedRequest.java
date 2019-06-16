package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.view.View;

public class PositionSelectedRequest implements Request {
    private Position mPosition;
    private View mView;

    public PositionSelectedRequest(Position position, View view) {
        mPosition = position;
        mView = view;
    }

    public Position getPosition() {
        return mPosition;
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
