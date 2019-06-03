package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;

public class Done extends Behaviour {
    @Override
    protected Expression continueEval(ShootContext context) {
        return this;
    }

    @Override
    public boolean isDone() {
        return true;
    }
}
