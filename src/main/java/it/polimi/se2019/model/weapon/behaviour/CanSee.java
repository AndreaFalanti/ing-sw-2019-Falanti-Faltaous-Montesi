package it.polimi.se2019.model.weapon.behaviour;

public class CanSee extends Expression {
    public CanSee() {
        super();
    }

    // TODO: add doc
    @Override
    public Expression continueEval(ShootContext shootContext) {
        return new RangeLiteral(shootContext.getBoard().getAllSeenBy(shootContext.getShooterPosition()));
    }
}
