package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

import java.util.Collections;

public class You extends Behaviour {
    @Override
    protected Expression continueEval(ShootContext context) {
        return new TargetsLiteral(Collections.singleton(context.getShooterColor()));
    }
}
