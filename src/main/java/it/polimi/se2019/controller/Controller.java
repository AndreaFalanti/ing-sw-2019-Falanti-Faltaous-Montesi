package it.polimi.se2019.controller;


import it.polimi.se2019.controller.response.MessageActionResponse;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.ShootResult;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.Optional;
import java.util.stream.Collectors;


public class Controller implements Observer<Request>, RequestHandler {

    // fields
    private Game mGame;
    private PerformPlayerAction mPerform;//change name
//  private requestPlayerAction mRequest;//change name
//  private getValidPosition mValidPostion;//change name
    private TakeLeaderboard mLeaderboard;
    private View mView;

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

    /*************************/
    /* WEAPON HELPER METHODS */
    /*************************/

    private void initShootContext() {
        if (mCurrentShootContext.isPresent())
            throw new IllegalStateException("Trying to initialize shoot context when one is already available!");

        mCurrentShootContext = Optional.of(new ShootContext(
                mGame.getBoard(),
                mGame.getPlayers().stream().collect(Collectors.toSet()), // TODO: see if type of mPlayers can be changed
                mGame.getActivePlayer().getColor()
        ));
    }

    private void continueShooting() {
        ShootResult result = mCurrentShootContext.eval();

        if (result.isComplete()) {
            result.asAction().perform(mGame);
        }
        else {
            Request request = mView.handle(result.asResponse());
            request.handleMe(this);
        }
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
        initShootContext();
        continueShooting();
    }

    @Override
    public void update(Request request) {
        request.handleMe(this);
    }
}
