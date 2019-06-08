package it.polimi.se2019.controller;


import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.HashSet;


public class Controller implements AbstractController {
    // fields
    private Game mGame;

    // constructors
    public Controller(Game game) {
        mGame = game;
    }

    // trivial getters
    public Game getGame() {
        return mGame;
    }

    /*******************/
    /* control methods */
    /*******************/

    public void shoot(View view, PlayerColor shooter, Expression weaponBehaviour) {
        // initialize context for shooting
        ShootContext initialContext = new ShootContext(
                view,
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
    public void handle(ValidPositionRequest request) {
        System.out.println("Delivering moves");
    }

    @Override
    public void handle(TargetsSelectedRequest request) {

    }

    @Override
    public void handle(ActionRequest actionRequest) {

    }

    @Override
    public void handle(PowerUpDiscardedRequest request) {

    }

    @Override
    public void handle(WeaponSelectedRequest request) {

    }

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Request message) {
        message.handleMe(this);
    }
}
