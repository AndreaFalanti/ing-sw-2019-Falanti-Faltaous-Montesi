package it.polimi.se2019.controller;


import it.polimi.se2019.controller.weapon.Weapons;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.*;

import java.util.HashSet;


public class Controller implements AbstractController {
    // fields
    private Game mGame;
    private PlayerActionController mPlayerActionController;

    // constructors
    public Controller(Game game) {
        mGame = game;
        mPlayerActionController = new PlayerActionController(this);
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
        ShootContext initialContext = new ShootContext(mGame, view, shooter);

        weaponBehaviour.eval(initialContext);
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
        shoot(
                request.getView(),
                request.getShooterColor(),
                // TODO: substitute with Weapons.get() call
                WeaponFactory.fromJson(Jsons.get("weapons/real/" + request.getWeaponID())).getBehaviour()
        );
    }

    /*****************************************/
    /* update method from observer interface */
    /*****************************************/

    @Override
    public void update(Request message) {
        message.handleMe(this);
    }
}
