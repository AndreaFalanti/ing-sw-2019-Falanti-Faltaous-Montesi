package it.polimi.se2019.model.action.responses;

import it.polimi.se2019.controller.ActionResponseHandler;
import it.polimi.se2019.controller.responses.Response;

public class MessageActionResponse implements InvalidActionResponse {
    private String mMessage;

    public MessageActionResponse(String message) {
        mMessage = message;
    }

    @Override
    public Response handle(ActionResponseHandler handler) {
        return handler.handle(this);
    }

    public String getMessage() {
        return mMessage;
    }
}
