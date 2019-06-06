package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

public class Store extends Behaviour {
    public Store(Expression name, Expression value) {
        super();

        putSub("name", name);
        putSub("value", value);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        context.setVar(
                getSub("name").asString(),
                getSub("value")
        );

        return getSub("value");
    }
}
