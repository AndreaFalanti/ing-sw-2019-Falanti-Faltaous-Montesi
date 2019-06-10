package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Intersect extends Behaviour {
    public Intersect() {

    }

    public Intersect(Expression lhs, Expression rhs) {
        putSub("lhs", lhs);
        putSub("rhs", rhs);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        // TODO: implement
        return null;
    }
}
