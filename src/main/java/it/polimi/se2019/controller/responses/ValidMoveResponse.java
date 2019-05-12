package it.polimi.se2019.controller.responses;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.view.ResponseHandler;

import java.util.*;


public class ValidMoveResponse implements Response {

    private List<Position> validPos;

    public ValidMoveResponse(List<Position> validPos) {
        this.validPos = validPos;
    }

    public List<Position> getValidPos() {
        return validPos;
    }

    @Override
    public void handle(ResponseHandler handler) { handler.handle(this);}
}