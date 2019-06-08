package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

public class Move extends Behaviour {
    public Move() {

    }

    public Move(Expression from, Expression to) {
        putSub("from", from);
        putSub("to", to);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        move(
                context,
                getSub("from").asTargets(),
                getSub("to").asPosition()
        );
    }
}
