package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Distance extends Behaviour {
    private static final String ORIGIN = "origin";
    private static final String AMOUNT = "amount";
    
    public Distance() {
        putSub(ORIGIN, new Pos(new You()));
    }

    public Distance(Expression origin, Expression amount) {
        putSub(ORIGIN, origin);
        putSub(AMOUNT, amount);
    }
    public Distance(Expression amount) {
        putSub(AMOUNT, amount);
    }

    @Override
    public final Expression eval(ShootContext context) {
        Expression moreGeneric = new DistanceRange(
                getSub(ORIGIN),
                getSub(AMOUNT),
                getSub(AMOUNT)
        );

        return moreGeneric.eval(context);
    }
}

