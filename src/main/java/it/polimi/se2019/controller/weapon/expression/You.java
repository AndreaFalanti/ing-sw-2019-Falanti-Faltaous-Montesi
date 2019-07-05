package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Behaviour that evaluates to the current shooter
 * @author Stefano Montesi
 */
public class You extends Behaviour {
    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        return new TargetLiteral(context.getShooterColor());
    }
}
