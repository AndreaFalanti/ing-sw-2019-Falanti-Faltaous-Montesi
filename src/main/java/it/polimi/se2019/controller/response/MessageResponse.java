package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;


public class MessageResponse implements Response {
    private String message;
    private boolean error;

    public MessageResponse() {
    }

    public String getMessage() {
        return null;
    }

    public boolean isError() {
        return false;
    }

    @Override
    public void handleMe(ResponseHandler handler) { handler.handle(this);}
}