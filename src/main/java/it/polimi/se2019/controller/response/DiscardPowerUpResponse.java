package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

public class DiscardPowerUpResponse implements Response {
    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
