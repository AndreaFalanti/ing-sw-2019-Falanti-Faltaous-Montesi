package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.view.View;

public class SelectOneDirection extends Behaviour {
    @Override
    public final Expression eval(ShootContext context) {
        return new DirectionLiteral(pickDirection(context));
    }
}
