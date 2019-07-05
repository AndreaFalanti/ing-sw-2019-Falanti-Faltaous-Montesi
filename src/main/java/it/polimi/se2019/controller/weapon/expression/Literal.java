package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

/**
 * Expression representing a literal. A literal is terminal, and though always evaluates to itself.
 * @param <T> the primitive type contained by the literal
 * @author Stefano Montesi
 */
public class Literal<T> extends Expression {
    private T mContents;


    /**
     * A literal has no subexpressions, but is initialized from the primitive value it wraps
     * @param contents the primitive contents of the literal
     */
    Literal(T contents) {
        super();

        mContents = contents;
    }

    // get access to the primitive
    T getPrimitive() {
        return mContents;
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        return this;
    }
}
