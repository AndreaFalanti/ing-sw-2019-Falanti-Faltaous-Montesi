package it.polimi.se2019.model.weapon.behaviour;

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
    protected Expression continueEval(ShootContext shootContext) {
        return this;
    }
}
