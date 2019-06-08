package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_LAST_SELECTED;

public class Difference extends Behaviour {
    public Difference() {

    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Expression moreGeneric = new NegateSelection(
                new Load(new StringLiteral(SPECIAL_VAR_LAST_SELECTED))
        );

        return moreGeneric.eval(context);
    }
}
