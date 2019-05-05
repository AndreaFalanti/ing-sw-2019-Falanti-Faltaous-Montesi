package it.polimi.se2019.controller.responses;

import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.view.ResponseHandler;

import java.util.*;


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
    public void handle(ResponseHandler handler) {

    }
}