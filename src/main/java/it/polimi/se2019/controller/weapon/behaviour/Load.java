package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

public class Load extends Behaviour {
    public Load() {

    }

    public Load(Expression name) {
        putSub("name", name);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        return context.getVar(getSub("name").asString());
    }
}
