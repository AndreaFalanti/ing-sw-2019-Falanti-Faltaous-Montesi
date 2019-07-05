package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Behaviour that evaluates to your position
 * @author Stefano Montesi
 */
public class YourPosition extends Behaviour {
    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        return new PositionLiteral(context.getShooterPosition());
    }
}
