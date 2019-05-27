package it.polimi.se2019.controller;


import it.polimi.se2019.controller.response.MessageActionResponse;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Controller implements Observer<RequestMessage>, RequestHandler {

    // fields
    private Game mGame;
    private PerformPlayerAction mPerform;//change name
//  private requestPlayerAction mRequest;//change name
//  private getValidPosition mValidPostion;//change name
    private TakeLeaderboard mLeaderboard;
    private View mView;
    final List<ShootInteraction> mShootInteractions = new ArrayList<>();

    // weapon related fields
    private Optional<ShootContext> mCurrentShootContext;

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

    /*******************/
    /* HANDLER METHODS */
    /*******************/

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
    public Response handle(ShootRequest request) {
        mShootInteractions.add(new ShootInteraction(this, mView, mGame, request));

        // TODO: consider if switching to void
        return null;
    }

    @Override
    public void update(RequestMessage message) {
        if (message.isAction()) {
            // TODO: Fala il codice del controller legato ad action andrebbe qui
            // TODO: forse servirà un mActionHandler per gestirle meglio o forse no...
            // Action action = message.asAction();
            // action.perform(mGame);
        }
        else if (message.isRequest())
            message.asRequest().handleMe(this);
    }
}
