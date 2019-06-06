package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

import java.util.Collections;

public class YourPosition extends Behaviour {
    @Override
    protected Expression continueEval(ShootContext context) {
        return new RangeLiteral(
                Collections.singleton(context.getShooterPosition())
        );
    }
}
