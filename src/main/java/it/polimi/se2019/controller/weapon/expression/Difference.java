package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.HashSet;
import java.util.Set;

public class Difference extends Behaviour {
    public Difference() {

    }

    public Difference(Expression lhs, Expression rhs) {
        putSub("lhs", lhs);
        putSub("rhs", rhs);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Set<Expression> lhsSet = new HashSet<>(getSub("lhs").asSetExpr().asSet());
        Set<Expression> rhsSet = new HashSet<>(getSub("rhs").asSetExpr().asSet());

        lhsSet = new HashSet<>(lhsSet);
        lhsSet.removeAll(rhsSet);

        return new SetExpression(lhsSet);
    }
}

