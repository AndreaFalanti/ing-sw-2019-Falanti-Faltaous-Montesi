package it.polimi.se2019.model.weapon.behaviour;

import java.util.List;

public class DoConsecutively extends Expression {
    private @SubExpressionList List<Expression> mDo;

    @Override
    protected Expression continueEval(ShootContext shootContext) {
        return new ActionLiteral(shootContext.getResultingAction());
    }
}
