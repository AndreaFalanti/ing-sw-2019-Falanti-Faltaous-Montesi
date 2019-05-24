package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.LeaderboardResponse;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;

import java.util.List;

public class TakeLeaderboard extends Controller{
    private List<Player> mLeaderboard;
    private Game mGame;

    public void takeLeaderboard(){
        new LeaderboardResponse(mGame.getLeaderboard());
        //TODO handleMe
    }

}
