package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Move extends Behaviour {
    public Move() {

    }

    public Move(Expression from, Expression to) {
        putSub("from", from);
        putSub("to", to);
    }

    @Override
    public final Expression eval(ShootContext context) {
        move(
                context,
                getSub("from").eval(context).asTargets(),
                getSub("to").eval(context).asPosition()
        );

        return new Done();
    }
}
