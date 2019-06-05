package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;

public class ValidPositionRequest implements Request {
    public ValidPositionRequest() {

    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
