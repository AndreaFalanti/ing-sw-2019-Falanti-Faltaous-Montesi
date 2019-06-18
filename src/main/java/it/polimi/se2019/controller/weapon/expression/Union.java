package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.HashSet;
import java.util.Set;

public class Union extends Behaviour {
    public Union() {

    }

    public Union(Expression lhs, Expression rhs) {
        putSub("lhs", lhs);
        putSub("rhs", rhs);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Set<Expression> lhsSet = new HashSet<>(getSub("lhs").asSetExpr().asSet());
        Set<Expression> rhsSet = new HashSet<>(getSub("rhs").asSetExpr().asSet());

        lhsSet = new HashSet<>(lhsSet);
        lhsSet.addAll(rhsSet);

        return new SetExpression(lhsSet);
    }
}
