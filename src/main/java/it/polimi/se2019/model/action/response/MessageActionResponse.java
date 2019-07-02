package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;

public class MessageActionResponse implements InvalidActionResponse {
    private String mMessage;
    private boolean mError;

    public MessageActionResponse(String message) {
        mMessage = message;
        mError = true;
    }

    public MessageActionResponse(String message, boolean error) {
        mMessage = message;
        mError = error;
    }

    @Override
    public void handle(InvalidActionResponseHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isError() {
        return mError;
    }
}
