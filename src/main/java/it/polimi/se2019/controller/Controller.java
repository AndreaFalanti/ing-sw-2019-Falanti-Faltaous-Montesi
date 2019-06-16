package it.polimi.se2019.controller;


import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.MoveShootAction;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import static sun.plugin.ClassLoaderInfo.reset;

public class Controller implements AbstractController {
    // fields
    private Game mGame;
    private PlayerActionController mPlayerActionController;
    private ShootInteraction mShootInteraction = new ShootInteraction();

    // constructors
    public Controller(Game game) {
        mGame = game;
        mPlayerActionController = new PlayerActionController(this);
    }

    // trivial getters
    public Game getGame() {
        return mGame;
    }
    public boolean isHandlingShootInteraction() {
        return mShootInteraction != null;
    }

    /*******************/
    /* control methods */
    /*******************/
    public void startShootInteraction(View view, PlayerColor shooter, Expression weaponBehaviour) {
        // no more than one player at a time can shoot
        //  NB. this is a consequence of the fact that every game has its own controller
        if (isHandlingShootInteraction()) {
            view.reportError("Only one player might shoot at one time!");
            return;
        }

        // a new thread is created for handling the shoot so that the view might continue to receive player
        // input on its thread
        mShootInteraction.exec(mGame, view, shooter, weaponBehaviour);
    }

    public void continueShootInteraction(Request request) {
        // shoot info is useless without the shooting
        if (isHandlingShootInteraction()) {
            request.getView().reportError("You can't provide shoot info with no shooting going on...");
            return;
        }

        // feed shooting information to current shoot interaction
        mShootInteraction.putRequest(request);
    }


    /******************************/
    /* handle requests from view  */
    /******************************/

    @Override
    public void handle(ValidPositionRequest request) {
        // deliver moves based on action
    }

    @Override
    public void handle(TargetsSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(ActionRequest actionRequest) {
        mPlayerActionController.executeAction(actionRequest.getAction(), actionRequest.getView());
    }

    @Override
    public void handle(PowerUpDiscardedRequest request) {

    }

    @Override
    public void handle(WeaponSelectedRequest request) {

    }

    @Override
    public void handle(ShootRequest request) {
        startShootInteraction(
                request.getView(),
                request.getShooterColor(),
                // TODO: substitute with Weapons.get() call
                WeaponFactory.fromJson(Jsons.get("weapons/real/" + request.getWeaponID())).getBehaviour()
        );
    }

    @Override
    public void handle(DirectionSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(PositionSelectedRequest request) {

    }

    @Override
    public void handle(EffectsSelectedRequest request) {

    }

    @Override
    public void handle(UndoWeaponInteractionRequest request) {

    }

    @Override
    public void handle(WeaponModeSelectedRequest request) {

    }

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Request message) {
        message.handleMe(this);
    }
}
