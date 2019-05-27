package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;

public class ValidMoveRequest implements Request {
    public ValidMoveRequest () {

    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
