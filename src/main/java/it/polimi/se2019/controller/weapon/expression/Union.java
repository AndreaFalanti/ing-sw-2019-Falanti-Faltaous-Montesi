package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.HashSet;
import java.util.Set;

/**
 * A behaviour evaluating to the union
 * @author Stefano Montesi
 */
public class Union extends Behaviour {
    public Union() {

    }

    public Union(Expression lhs, Expression rhs) {
        putSub("lhs", lhs);
        putSub("rhs", rhs);
    }

    @Override
    public final Expression eval(ShootContext context) {
        Set<Expression> lhsSet = new HashSet<>(getSub("lhs").eval(context).asSetExpr().asSet());
        Set<Expression> rhsSet = new HashSet<>(getSub("rhs").eval(context).asSetExpr().asSet());

        lhsSet = new HashSet<>(lhsSet);
        lhsSet.addAll(rhsSet);

        return new SetExpression(lhsSet);
    }
}
