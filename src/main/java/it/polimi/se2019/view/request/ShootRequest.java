package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.behaviour.AtomicExpression;

public class ShootRequest implements Request {
    private AtomicExpression mBehavoiur;
    private PlayerColor mShooterColor;

    public AtomicExpression getBehaviour() {
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
