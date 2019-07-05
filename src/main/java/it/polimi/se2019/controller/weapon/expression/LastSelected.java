package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_LAST_SELECTED;

/**
 * Behaviour evaluating to the TargetLiteral that was last selected by a SelectTargets behaviour
 * @author Stefano Montesi
 */
public class LastSelected extends Behaviour {
    // required for Gson; should never be called by the user
    public LastSelected() {
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        return context.getVar(SPECIAL_VAR_LAST_SELECTED);
    }
}
