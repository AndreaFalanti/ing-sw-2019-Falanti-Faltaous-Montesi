package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

public class GetVisibleRange extends Behaviour {
    public GetVisibleRange(Expression observer) {
        putSub("observer", observer);
    }

    public GetVisibleRange() {
    }

    @Override
    protected Expression handleSubDefaultValue(String subName, ShootContext context) {
        if (subName.equals("observer")) {
            return new You();
        }

        return super.handleSubDefaultValue(subName, context);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        return new RangeLiteral(context.getBoard().getAllSeenBy(context.getShooterPosition()));
    }
}
