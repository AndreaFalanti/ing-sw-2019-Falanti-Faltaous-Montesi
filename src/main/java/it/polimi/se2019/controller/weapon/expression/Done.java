package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Expression symbolizing that an evaluation has been done. Kind of like a void return type.
 * @author Stefano Montesi
 */
public class Done extends Behaviour {
    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        return this;
    }

    /**
     * True if evaluation is considered done
     */
    @Override
    public boolean isDone() {
        return true;
    }
}
