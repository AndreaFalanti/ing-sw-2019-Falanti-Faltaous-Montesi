package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.HashSet;
import java.util.Set;

/**
 * Behaviour that evaluates to the difference of two sets passed as subexpressions
 * @author Stefano Montesi
 */
public class Difference extends Behaviour {
    // necessary for Gson; should never be used
    public Difference() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param lhs SetExpression comprising the left hand side of the operation
     * @param rhs SetExpression comprising right hand side of the operation
     */
    public Difference(Expression lhs, Expression rhs) {
        putSub("lhs", lhs);
        putSub("rhs", rhs);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Set<Expression> lhsSet = new HashSet<>(getSub("lhs").eval(context).asSetExpr().asSet());
        Set<Expression> rhsSet = new HashSet<>(getSub("rhs").eval(context).asSetExpr().asSet());

        lhsSet = new HashSet<>(lhsSet);
        lhsSet.removeAll(rhsSet);

        return new SetExpression(lhsSet);
    }
}

