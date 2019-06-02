package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;

public class LeaderboardRequest implements Request {
    public LeaderboardRequest() {
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
