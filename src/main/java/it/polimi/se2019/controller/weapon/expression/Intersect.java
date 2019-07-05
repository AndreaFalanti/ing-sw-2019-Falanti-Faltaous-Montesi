package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.HashSet;
import java.util.Set;

/**
 * Behaviour that evaluates to the intersection of two generic SetExpressions provided as its subexpressions
 * @author Stefano Montesi
 */
public class Intersect extends Behaviour {
    // required for Gson; should never be called by the user
    public Intersect() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param lhs SetExpression comprising left hand side of the set intersection
     * @param rhs SetExpression comprising left hand side of the set intersection
     */
    public Intersect(Expression lhs, Expression rhs) {
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
        lhsSet.retainAll(rhsSet);

        return new SetExpression(lhsSet);
    }
}
