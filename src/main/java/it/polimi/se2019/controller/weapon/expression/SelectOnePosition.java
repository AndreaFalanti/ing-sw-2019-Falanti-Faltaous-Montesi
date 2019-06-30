package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.view.View;

public class SelectOnePosition extends Behaviour {
    public SelectOnePosition() {

    }

    public SelectOnePosition(Expression from) {
        putSub("from", from);
    }

    @Override
    public final Expression eval(ShootContext context) {
        ShootInteraction interaction = context.getInteraction();
        View view = context.getView();

        Position selectedPosition = interaction.selectPosition(view, getSub("from").eval(context).asRange());

        return new PositionLiteral(selectedPosition);
    }
}
