package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;

public class SelectOnePosition extends Behaviour {
    public SelectOnePosition() {

    }

    public SelectOnePosition(Expression from) {
        putSub("from", from);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Position selectedPosition = selectPosition(context, getSub("from").asRange());

        return new PositionLiteral(selectedPosition);
    }
}
