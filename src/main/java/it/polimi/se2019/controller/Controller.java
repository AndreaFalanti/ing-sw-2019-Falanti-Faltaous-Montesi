package it.polimi.se2019.controller;


import it.polimi.se2019.controller.responses.MessageActionResponse;
import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.requests.*;


public class Controller implements Observer<Request>, RequestHandler {

    private Game mGame;
    private PerformPlayerAction mPerform;//change name
//  private requestPlayerAction mRequest;//change name
//  private getValidPosition mValidPostion;//change name
    private TakeLeaderboard mLeaderboard;
    private View mRemoteView;

    public Controller() {
    }

    public void performPlayerAction(Action action) {

    }

    public void requestPlayerAction() {

    }

    public Position[] getValidPosition(PlayerColor color){
        return null;
    }//return all valid positions according to action


    public void getLeaderBoard(){

    }

    @Override
    public Response handle(GrabRequest request) {
        return null;
    }

    @Override
    public Response handle(ReloadRequest request) {
        return null;
    }

    @Override
    public Response handle(LeaderboardRequest request) {
        System.out.println("Delivering leaderboard");
        return null;
    }

    @Override
    public Response handle(ValidMoveRequest request) {
        System.out.println("Delivering moves");
        return null;
    }

    @Override
    public Response handle(MessageActionResponse request) {
        return null;
    }

    @Override
    public void update(Request request) {
        request.handleMe(this);
    }
}