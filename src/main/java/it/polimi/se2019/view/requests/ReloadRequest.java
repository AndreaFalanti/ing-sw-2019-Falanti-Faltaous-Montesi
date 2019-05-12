package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.controller.responses.Response;

public class ReloadRequest implements Request {
    private int weaponIndex;

    public ReloadRequest() {
    }

    public int getWeaponIndex() {
        return weaponIndex;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
