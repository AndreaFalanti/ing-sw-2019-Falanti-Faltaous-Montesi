package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Behaviour that evaluates to a range at a specified distance from a specified origin
 * @author Stefano Montesi
 */
public class Distance extends Behaviour {
    private static final String ORIGIN = "origin";
    private static final String AMOUNT = "amount";

    // required for Gson; should never be called by the user
    public Distance() {
        putSub(ORIGIN, new Pos(new You()));
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param origin position from which the distance is taken
     * @param amount desired distance from origin
     */
    public Distance(Expression origin, Expression amount) {
        putSub(ORIGIN, origin);
        putSub(AMOUNT, amount);
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     *  NB. the origin subexpression evaluates to the position of the shooter by default
     * @param amount desired distance from origin
     */
    public Distance(Expression amount) {
        putSub(AMOUNT, amount);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
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

