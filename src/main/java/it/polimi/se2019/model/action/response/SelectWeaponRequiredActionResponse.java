package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;

public class SelectWeaponRequiredActionResponse extends MessageActionResponse {
    public SelectWeaponRequiredActionResponse(String message) {
        super(message);
    }

    @Override
    public void handle(InvalidActionResponseHandler handler) {
        handler.handle(this);
    }
}
