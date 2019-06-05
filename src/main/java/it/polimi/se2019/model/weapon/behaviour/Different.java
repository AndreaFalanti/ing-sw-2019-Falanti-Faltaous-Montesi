package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;

public class Different extends Behaviour {
    @Override
    protected Expression continueEval(ShootContext context) {
        Expression moreGeneric = new NegateSelection(
                new Look(new StringLiteral(LAST_SELECTED_TARGETS_VAR))
        );

        return moreGeneric.eval(context);
    }
}
