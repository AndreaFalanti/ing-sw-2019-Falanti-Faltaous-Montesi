package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;

public class LeaderboardRequest implements Request {
    public LeaderboardRequest() {
    }

    @Override
    public void handle(RequestHandler handler) {
        handler.handle(this);
    }
}
