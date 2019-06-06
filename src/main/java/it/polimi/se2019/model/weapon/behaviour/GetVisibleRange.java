package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

public class GetVisibleRange extends Behaviour {
    @Override
    protected Expression continueEval(ShootContext context) {
        return new RangeLiteral(context.getBoard().getAllSeenBy(context.getShooterPosition()));
    }
}
