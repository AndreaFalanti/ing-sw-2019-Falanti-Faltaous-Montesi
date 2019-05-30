package it.polimi.se2019.model.weapon.behaviour;

public class GetVisibleRange extends Expression {
    @Override
    protected Expression continueEval(ShootContext shootContext) {
        return new RangeLiteral(shootContext.getBoard().getAllSeenBy(shootContext.getShooterPosition()));
    }
}
