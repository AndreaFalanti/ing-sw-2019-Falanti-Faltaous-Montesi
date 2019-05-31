package it.polimi.se2019.model.weapon.behaviour;

import java.util.List;

public class DoConsecutively extends AtomicExpression {
    private @SubExpressionList List<AtomicExpression> mDo;

    @Override
    protected AtomicExpression continueEval(ShootContext shootContext) {
        return new ActionLiteral(shootContext.getResultingAction());
    }
}
