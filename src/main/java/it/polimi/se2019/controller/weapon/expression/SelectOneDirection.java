package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.view.View;

/**
 * Behaviour that takes a direction from player input
 * @author Stefano Montesi
 */
public class SelectOneDirection extends Behaviour {
    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        ShootInteraction interaction = context.getInteraction();
        View view = context.getView();

        return new DirectionLiteral(interaction.pickDirection(view));
    }
}
