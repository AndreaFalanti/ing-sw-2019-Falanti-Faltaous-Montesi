package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;

public class MessageActionResponse implements InvalidActionResponse {
    private String mMessage;

    public MessageActionResponse(String message) {
        mMessage = message;
    }

    @Override
    public void handle(InvalidActionResponseHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return mMessage;
    }
}
