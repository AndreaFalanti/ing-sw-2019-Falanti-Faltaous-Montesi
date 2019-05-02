package it.polimi.se2019.model.weapon.behaviour;

public class CanSee implements Expression {
    @Override
    public Expression eval(Context context) {
        return new RangeLiteral(context.getBoard().getAllSeenBy(context.getShooterPosition()));
    }
}
