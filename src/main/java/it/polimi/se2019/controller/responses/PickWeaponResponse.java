package it.polimi.se2019.controller.responses;

import it.polimi.se2019.view.ResponseHandler;


public class PickWeaponResponse implements Response {


    public PickWeaponResponse() {
    }

    @Override
    public void handle(ResponseHandler handler) { handler.handle(this);}
}