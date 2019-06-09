package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

public class Distance extends Behaviour {
    public Distance() {

    }

    public Distance(Expression amount) {
        putSub("amount", amount);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Expression moreGeneric = new DistanceRange(
                getSub("amount"),
                getSub("amount")
        );

        return moreGeneric.eval(context);
    }
}

