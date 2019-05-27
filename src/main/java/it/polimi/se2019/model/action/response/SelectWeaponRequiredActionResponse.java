package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;
import it.polimi.se2019.controller.response.Response;

public class SelectWeaponRequiredActionResponse extends MessageActionResponse {
    public SelectWeaponRequiredActionResponse(String message) {
        super(message);
    }

    @Override
    public Response handle(InvalidActionResponseHandler handler) {
        return handler.handle(this);
    }
}
