package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Done extends Behaviour {
    @Override
    public final Expression eval(ShootContext context) {
        return this;
    }

    @Override
    public boolean isDone() {
        return true;
    }
}
