package it.polimi.se2019.controller;


import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.util.Either;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.ArrayDeque;
import java.util.Deque;


public class Controller implements AbstractController {

    // fields
    private Game mGame;
    private PerformPlayerAction mPerform;//change name
//  private requestPlayerAction mRequest;//change name
//  private getValidPosition mValidPostion;//change name
    private TakeLeaderboard mLeaderboard;
    private View mView;
    final Deque<ShootInteractionController> mShootInteractions = new ArrayDeque<>();

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
    /* other requests  */
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
    public Response handle(ShootRequest request) {
        mShootInteractions.push(new ShootInteractionController(this, mView, mGame, request));

        // TODO: consider if switching to void
        return null;
    }

    /*********************************************************/
    /* requests that should be handled by shoot interactions */
    /*********************************************************/

    public void handleShootRequest(Request request) {
        if (mShootInteractions.isEmpty())
            throw new IllegalStateException(
                    "Cannot handle request related to shooting with no shooting interaction in progess!"
            );

        request.handleMe(mShootInteractions.peek());
    }

    @Override
    public Response handle(TargetsSelectedRequest request) {
        handleShootRequest(request);

        return null;
    }

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Either<Request, Action> message) {
        message.apply(
                request -> request.handleMe(this),
                action -> {
                    // TODO: Fala il codice del controller legato ad action andrebbe qui
                    // TODO: forse servirà un mActionHandler per gestirle meglio o forse no...
                    // Action action = message.asAction();
                    // action.perform(mGame);
                }
        );
    }
}
