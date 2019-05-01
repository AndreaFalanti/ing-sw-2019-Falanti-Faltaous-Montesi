package it.polimi.se2019.model.weapon_behaviour;

public class CanSee extends Expression {
    @Override
    public Expression eval(Context context) {
        return new RangeLiteral(context.getBoard().getAllSeenBy(context.getShooterPosition()));
    }
}
