package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.board.Direction;
import it.polimi.se2019.view.View;

public class SelectOneDirection extends Behaviour {
    @Override
    protected Expression continueEval(ShootContext context) {
        View view = context.getView();

        return new DirectionLiteral(view.pickDirection());
    }
}
