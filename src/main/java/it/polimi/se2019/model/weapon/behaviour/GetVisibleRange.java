package it.polimi.se2019.model.weapon.behaviour;

public class GetVisibleRange extends AtomicExpression {
    @Override
    protected AtomicExpression continueEval(ShootContext shootContext) {
        return new RangeLiteral(shootContext.getBoard().getAllSeenBy(shootContext.getShooterPosition()));
    }
}
