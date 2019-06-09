package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

public class Literal<PrimitiveType> extends Expression {
    protected PrimitiveType mContents;

    // a literal has no subexpressions, but is initialized from the primitive value it wraps
    public Literal(PrimitiveType contents) {
        super();

        mContents = contents;
    }

    // get access to the primitive
    public PrimitiveType getPrimitive() {
        return mContents;
    }

    // a literal usually evaluates to itself
    @Override
    public Expression eval(ShootContext context) {
        return this;
    }
}
