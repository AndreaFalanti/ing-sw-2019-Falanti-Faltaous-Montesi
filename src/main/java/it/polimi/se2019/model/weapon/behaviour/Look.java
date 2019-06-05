package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;

public class Look extends Behaviour {

    public Look(Expression name) {
        super();

        putSub("name", name);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        return context.getVar(getSub("name").asString());
    }
}
