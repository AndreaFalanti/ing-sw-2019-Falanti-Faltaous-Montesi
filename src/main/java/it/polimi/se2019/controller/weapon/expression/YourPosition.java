package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.Collections;

public class YourPosition extends Behaviour {
    @Override
    protected Expression continueEval(ShootContext context) {
        return new PositionLiteral(context.getShooterPosition());
    }
}
