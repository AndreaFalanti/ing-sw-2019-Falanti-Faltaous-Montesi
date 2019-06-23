package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Load extends Behaviour {
    public Load() {

    }

    public Load(Expression name) {
        putSub("name", name);
    }

    @Override
    public final Expression eval(ShootContext context) {
        return context.getVar(getSub("name").eval(context).asString());
    }
}
