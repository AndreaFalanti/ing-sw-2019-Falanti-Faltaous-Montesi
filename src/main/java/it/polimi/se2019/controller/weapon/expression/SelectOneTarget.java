package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Behaviour that takes a one target from player input
 */
public class SelectOneTarget extends Behaviour {
    // required for Gson; should never be called by the user
    public SelectOneTarget() {
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @from the targets among which to select
     */
    public SelectOneTarget(Expression from) {
        putSub("from", from);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        // use generic expression to synthesise this
        Expression moreGeneric = new SelectTargets(
                new IntLiteral(1),
                new IntLiteral(1),
                getSub("from")
        );

        return moreGeneric.eval(context);
    }
}

