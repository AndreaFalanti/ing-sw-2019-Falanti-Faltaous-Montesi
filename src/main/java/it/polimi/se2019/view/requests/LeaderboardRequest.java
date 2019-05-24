package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.controller.responses.Response;

public class LeaderboardRequest implements Request {
    public LeaderboardRequest() {
    }

    @Override
    public Response handleMe(RequestHandler handler) {
        return handler.handle(this);
    }
}
