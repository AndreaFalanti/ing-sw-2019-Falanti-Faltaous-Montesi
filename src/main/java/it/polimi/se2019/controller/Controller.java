package it.polimi.se2019.controller;


import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.MoveAction;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
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

    public Controller() {
    }

    /**
     * Move a player using a weapon
     * @param playerToMove player to move
     * @param newPosition new position of moved player
     */
    public void movePlayerWithWeapon(PlayerColor playerToMove, Position newPosition) {
        // the move is not normal since no "move action" is consumed by the player
        new MoveAction(playerToMove, newPosition, false).perform(mGame);
    }

    public void commenceShootInteraction(PlayerColor shooter, Expression weaponBehaviour) {
        // initialize context for shooting
        ShootContext initialContext = new ShootContext(
                mGame.getBoard(),
                new HashSet<>(mGame.getPlayers()),
                shooter
        );

        weaponBehaviour.eval(initialContext);
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

    /******************************/
    /* handle requests from view  */
    /******************************/

    @Override
    public Response handle(ShootRequest request) {
        request.getBehaviour().eval();
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
