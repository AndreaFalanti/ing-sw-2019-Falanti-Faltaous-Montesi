package it.polimi.se2019.controller.responses;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.view.ResponseHandler;

import java.util.*;


public class MessageActionResponse implements Response {

    private String message;

    public MessageActionResponse(String message) {
        this.message = message;
    }

    @Override
    public void handle(ResponseHandler handler) { handler.handle(this);}
}