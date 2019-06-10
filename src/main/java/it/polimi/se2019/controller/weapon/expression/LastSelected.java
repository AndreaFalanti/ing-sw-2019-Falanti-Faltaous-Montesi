package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_LAST_SELECTED;

public class LastSelected extends Behaviour {
    public LastSelected() {
        // TODO: implement
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        return context.getVar(SPECIAL_VAR_LAST_SELECTED);
    }
}
