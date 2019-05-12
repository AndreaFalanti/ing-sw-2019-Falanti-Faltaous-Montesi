package it.polimi.se2019.model.action.responses;

import it.polimi.se2019.controller.ActionResponseHandler;
import it.polimi.se2019.controller.responses.Response;

public class DiscardRequiredActionResponse extends MessageActionResponse {
    public DiscardRequiredActionResponse(String message) {
        super(message);
    }

    @Override
    public Response handle(ActionResponseHandler handler) {
        return handler.handle(this);
    }
}
