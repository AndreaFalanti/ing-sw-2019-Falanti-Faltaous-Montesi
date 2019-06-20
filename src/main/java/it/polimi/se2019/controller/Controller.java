package it.polimi.se2019.controller;


import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

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

    /*******************/
    /* control methods */
    /*******************/
    public void startShootInteraction(View view, PlayerColor shooter, Expression weaponBehaviour) {
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

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Request message) {
        message.handleMe(this);
    }
}
