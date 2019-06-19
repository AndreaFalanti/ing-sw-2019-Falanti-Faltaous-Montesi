package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Neighbours extends Behaviour {
    public Neighbours() {
        putSub("who", new You());
    }

    public Neighbours(Expression who) {
        putSub("who", who);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Expression moreGeneric = new All(new Pos(getSub("who")));

        return moreGeneric.eval(context);
    }
}
