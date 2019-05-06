package it.polimi.se2019.model.weapon.behaviour;

public class CanSee implements Expression {
    @Override
    public Expression eval(ShootContext shootContext) {
        return new RangeLiteral(shootContext.getBoard().getAllSeenBy(shootContext.getShooterPosition()));
    }
}
