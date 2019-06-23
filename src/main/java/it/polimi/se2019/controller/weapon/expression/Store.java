package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Store extends Behaviour {
    public Store(Expression name, Expression value) {
        super();

        putSub("name", name);
        putSub("value", value);
    }

    @Override
    public final Expression eval(ShootContext context) {
        Expression evaluatedValue = getSub("value").eval(context);

        context.setVar(
                getSub("name").eval(context).asString(),
                evaluatedValue
        );

        return evaluatedValue;
    }
}
