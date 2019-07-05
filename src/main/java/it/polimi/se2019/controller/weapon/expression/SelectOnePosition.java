package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.view.View;

/**
 * Behaviour that takes a position from player input
 */
public class SelectOnePosition extends Behaviour {
    // required for Gson; should never be called by the user
    public SelectOnePosition() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param from positions among which the player can select
     */
    public SelectOnePosition(Expression from) {
        putSub("from", from);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        ShootInteraction interaction = context.getInteraction();
        View view = context.getView();

        Position selectedPosition = interaction.selectPosition(view, getSub("from").eval(context).asRange());

        return new PositionLiteral(selectedPosition);
    }
}
