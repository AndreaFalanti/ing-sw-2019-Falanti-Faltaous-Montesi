package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;

public class ValidMoveRequest implements Request {
    public ValidMoveRequest () {

    }

    @Override
    public void handle(RequestHandler handler) {
        handler.handle(this);
    }
}
