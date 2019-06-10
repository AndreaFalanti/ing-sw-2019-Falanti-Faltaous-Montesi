package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.expression.Expression;

public class XorEffect extends Expression {
    public XorEffect() {

    }

    private Expression mLhs;
    private Expression mRhs;

    @Override
    public Expression eval(ShootContext context) {
        // TODO: implement this when things are decided
        return null;
    }
}
