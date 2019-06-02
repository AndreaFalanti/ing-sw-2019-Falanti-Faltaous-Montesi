package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;
import it.polimi.se2019.controller.response.Response;

public class DiscardRequiredActionResponse extends MessageActionResponse {
    public DiscardRequiredActionResponse(String message) {
        super(message);
    }

    @Override
    public Response handle(InvalidActionResponseHandler handler) {
        return handler.handle(this);
    }
}
