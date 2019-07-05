package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_LAST_SELECTED;

/**
 * Behaviour that evaluates to the remaining not previously selected targets.
 * @author Stefano Montesi
 */
public class Others extends Behaviour {
    // required for Gson; should never be called by the user
    public Others() {
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Expression moreGeneric = new NegateTargets(new Load(new StringLiteral(SPECIAL_VAR_LAST_SELECTED)));

        return moreGeneric.eval(context);
    }
}
