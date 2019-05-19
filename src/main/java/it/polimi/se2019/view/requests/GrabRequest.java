package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.model.Position;

public class GrabRequest implements Request {
    private Position pos;

    public GrabRequest() {
    }

    public Position getPos() {
        return pos;
    }

    @Override
    public Response handle(RequestHandler handler) {
        return handler.handle(this);
    }
}
