package it.polimi.se2019.model.weapon.behaviour;

public class IntLiteral implements Expression {
    int mContents;

    public IntLiteral(int contents) {
        mContents = contents;
    }

    @Override
    public Expression eval(ShootContext shootContext) {
        return this;
    }

    @Override
    public int asInt() {
        return mContents;
    }
}
