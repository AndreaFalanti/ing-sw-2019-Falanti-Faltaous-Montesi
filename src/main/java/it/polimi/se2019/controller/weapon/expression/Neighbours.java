package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Behaviour that evaluates to a set containing the players standing in the same tile
 * of {@code who}
 * @author Stefano Montesi
 */
public class Neighbours extends Behaviour {
    // required for Gson; should never be called by the user
    public Neighbours() {
        putSub("who", new You());
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param who player who's neighbours you are interested in
     */
    public Neighbours(Expression who) {
        putSub("who", who);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Expression moreGeneric = new All(new Pos(getSub("who")));

        return moreGeneric.eval(context);
    }
}
