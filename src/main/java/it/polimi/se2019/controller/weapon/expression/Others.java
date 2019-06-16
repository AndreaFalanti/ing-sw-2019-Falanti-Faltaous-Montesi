package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_LAST_SELECTED;

public class Others extends Behaviour {
    public Others() {
        // TODO: implement
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Expression moreGeneric = new NegateTargets(new Load(new StringLiteral(SPECIAL_VAR_LAST_SELECTED)));

        return moreGeneric.eval(context);
    }
}
