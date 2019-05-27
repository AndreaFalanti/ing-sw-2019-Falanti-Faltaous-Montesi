package it.polimi.se2019.controller.response;

import it.polimi.se2019.model.action.ResponseCode;
import it.polimi.se2019.view.ResponseHandler;


public class MessageActionResponse implements Response {

    private ResponseCode mReturnCode;

    public MessageActionResponse(ResponseCode code){ this.mReturnCode = code; }

    @Override
    public void handleMe(ResponseHandler handler) { handler.handle(this);}
}