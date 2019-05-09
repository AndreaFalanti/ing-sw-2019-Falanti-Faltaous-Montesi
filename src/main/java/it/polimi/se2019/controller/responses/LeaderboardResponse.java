package it.polimi.se2019.controller.responses;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.ResponseHandler;

import java.util.Map;

public class LeaderboardResponse implements Response {

    private Map<PlayerColor, Integer> leaderboard;

    public void LeaderboardResponse(Map<PlayerColor, Integer> leaderboard){
        this.leaderboard = leaderboard;
    }

    @Override
    public void handle(ResponseHandler handler) { handler.handle(this);}
}