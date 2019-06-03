package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.behaviour.Behaviour;

public class ShootRequest implements Request {
    private Behaviour mBehavoiur;
    private PlayerColor mShooterColor;

    public Behaviour getBehaviour() {
        return mBehavoiur;
    }

    public PlayerColor getShooterColor() {
        return mShooterColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
