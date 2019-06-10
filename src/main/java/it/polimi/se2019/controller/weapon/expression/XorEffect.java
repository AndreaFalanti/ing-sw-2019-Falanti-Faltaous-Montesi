package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.controller.weapon.ShootContext;

public class XorEffect extends Expression {
    public XorEffect() {

    }

    private Effect mLhs;
    private Effect mRhs;

    @Override
    public Expression eval(ShootContext context) {
        // TODO: implement this when things are decided
        return null;
    }
}
