package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class Literal<T> extends Expression {
    private T mContents;

    // a literal has no subexpressions, but is initialized from the primitive value it wraps
    Literal(T contents) {
        super();

        mContents = contents;
    }

    // get access to the primitive
    T getPrimitive() {
        return mContents;
    }

    // a literal usually evaluates to itself
    @Override
    public final Expression eval(ShootContext context) {
        return this;
    }
}
