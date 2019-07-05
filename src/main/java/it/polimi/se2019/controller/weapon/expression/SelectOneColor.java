package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.View;

/**
 * Behaviour that takes a color from layer input
 * @author Stefano Montesi
 */
public class SelectOneColor extends Behaviour {
    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public Expression eval(ShootContext context) {
        View view = context.getView();
        ShootInteraction interaction = context.getInteraction();

        Expression from = getSub("from").eval(context);

        TileColor color = interaction.pickRoomColor(
                view,
                from.asColors()
        );

        return new ColorLiteral(color);
    }
}
