package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class You extends Behaviour {
    @Override
    public final Expression eval(ShootContext context) {
        return new TargetLiteral(context.getShooterColor());
    }
}
