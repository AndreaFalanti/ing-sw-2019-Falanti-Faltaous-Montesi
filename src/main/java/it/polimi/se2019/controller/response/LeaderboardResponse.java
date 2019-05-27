package it.polimi.se2019.controller.response;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.view.ResponseHandler;

import java.util.List;

public class LeaderboardResponse implements Response {

    private List<Player> leaderboard;

    public LeaderboardResponse(List<Player> leaderboard){
        this.leaderboard = leaderboard;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}