package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.Position;

public class GrabRequest implements Request {
    private Position mPos;

    public GrabRequest(Position pos) {
        mPos = pos;
    }

    public Position getPos() {
        return mPos;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
