package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class InfLiteral extends Expression {
    @Override
    public Expression eval(ShootContext context) {
        return this;
    }

    @Override
    public boolean isInf() {
        return true;
    }
}
