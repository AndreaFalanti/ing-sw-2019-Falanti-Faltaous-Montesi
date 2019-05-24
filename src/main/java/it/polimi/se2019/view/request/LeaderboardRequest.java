package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.controller.response.Response;

public class LeaderboardRequest implements Request {
    public LeaderboardRequest() {
    }

    @Override
    public Response handleMe(RequestHandler handler) {
        return handler.handle(this);
    }
}
