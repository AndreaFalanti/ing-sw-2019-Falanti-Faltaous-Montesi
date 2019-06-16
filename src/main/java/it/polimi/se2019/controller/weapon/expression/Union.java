package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Union extends Behaviour {
    public Union() {

    }

    public Union(Expression lhs, Expression rhs) {
        putSub("lhs", lhs);
        putSub("rhs", rhs);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        throw new UnsupportedOperationException("WIP");
    }
}
