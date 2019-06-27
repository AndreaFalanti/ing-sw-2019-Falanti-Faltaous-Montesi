package it.polimi.se2019.controller;


import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.CostlyAction;
import it.polimi.se2019.model.action.MoveGrabAction;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements AbstractController {
    // messages constants
    public static final String NO_ACTIONS_REMAINING_ERROR_MSG = "No actions remaining! Undo and try again...";

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    // fields
    private final Game mGame;
    private final PlayerActionController mPlayerActionController;
    private final Map<PlayerColor, View> mPlayerViews;
    private final ShootInteraction mShootInteraction;

    private WeaponIndexStrategy mWeaponIndexStrategy;

    // constructors
    public Controller(Game game, Map<PlayerColor, View> playerViews) {
        mGame = game;
        mPlayerViews = playerViews;
        mPlayerActionController = new PlayerActionController(this);
        mShootInteraction = new ShootInteraction(mGame, mPlayerViews);
    }

    // trivial getters
    public Game getGame() {
        return mGame;
    }

    public ShootInteraction getShootInteraction() {
        return mShootInteraction;
    }

    public Object getShootInteractionLock() {
        if (mShootInteraction.isOccupied())
            return mShootInteraction.getLock();
        else
            throw new UnsupportedOperationException("Trying to get lock of a non occupied shoot interaction!");
    }

    public boolean isHandlingShootInteraction() {
        return mShootInteraction.isOccupied();
    }

    // Setters
    public void setWeaponIndexStrategy(WeaponIndexStrategy weaponIndexStrategy) {
        mWeaponIndexStrategy = weaponIndexStrategy;
    }


    /*******************/
    /* control methods */
    /*******************/
    // TODO: make this private and notify ShootRequest in weapon tests
    public void startShootInteraction(PlayerColor shooter, Expression weaponBehaviour) {
        mShootInteraction.exec(mGame, shooter, weaponBehaviour);
    }

    private void continueShootInteraction(Request request) {
        // shoot info is useless without the shooting
        if (!isHandlingShootInteraction()) {
            mPlayerViews.get(request.getViewColor()).reportError("You can't provide shoot info with no shooting going on...");
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
        mPlayerActionController.executeAction(actionRequest.getAction(), mPlayerViews.get(actionRequest.getViewColor()));
    }

    @Override
    public void handle(PowerUpDiscardedRequest request) {
        logger.log(Level.INFO, "Handling power up discard selection. PowerUps discarded: {0}",
                Arrays.toString(request.getDiscarded()));

        CostlyAction costlyAction = mPlayerActionController.getCompletableCostlyAction();
        if (costlyAction != null) {
            costlyAction.setDiscardedCards(request.getDiscarded());

            mPlayerActionController.executeAction(mPlayerActionController.getCachedAction(),
                    mPlayerViews.get(request.getViewColor()));
        }
    }

    @Override
    public void handle(WeaponSelectedRequest request) {
        logger.log(Level.INFO, "Handling weapon selection. Index selected: {0}", request.getWeaponIndex());

        mPlayerActionController.setCompletableGrabAction(mWeaponIndexStrategy.completeAction(request.getWeaponIndex(),
                mPlayerActionController.getCompletableGrabAction()));
        if (mPlayerActionController.getCachedAction().isComposite()) {
            ((MoveGrabAction) mPlayerActionController.getCachedAction()).setGrabAction(
                    mPlayerActionController.getCompletableGrabAction()
            );
        }
        else {
            mPlayerActionController.setCachedAction(mPlayerActionController.getCompletableGrabAction());
        }

        mWeaponIndexStrategy = null;
        mPlayerActionController.executeAction(mPlayerActionController.getCachedAction(),
                mPlayerViews.get(request.getViewColor()));
    }

    @Override
    public void handle(ShootRequest request) {
        startShootInteraction(
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
        continueShootInteraction(request);
    }

    @Override
    public void handle(EffectsSelectedRequest request) {
        continueShootInteraction(request);
    }

    @Override
    public void handle(UndoWeaponInteractionRequest request) {

    }

    @Override
    public void handle(WeaponModeSelectedRequest request) {

    }

    @Override
    public void handle(PowerUpSelectedRequest request) {

    }

    @Override
    public void handle(RoomSelectedRequest request) {

    }

    @Override
    public void handle(TurnEndRequest request) {
        mGame.onTurnEnd();
        mGame.startNextTurn();
    }

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Request message) {
        message.handleMe(this);
    }
}
