package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Distance extends Behaviour {
    public Distance() {
        putSub("origin", new You());
    }

    public Distance(Expression origin, Expression amount) {
        putSub("origin", origin);
        putSub("amount", amount);
    }
    public Distance(Expression amount) {
        putSub("amount", amount);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Expression moreGeneric = new DistanceRange(
                getSub("origin"),
                getSub("amount"),
                getSub("amount")
        );

        return moreGeneric.eval(context);
    }
}

