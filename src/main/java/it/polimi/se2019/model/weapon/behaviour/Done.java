package it.polimi.se2019.model.weapon.behaviour;

public class Done extends AtomicExpression {
    @Override
    protected AtomicExpression continueEval(ShootContext shootContext) {
        return this;
    }

    @Override
    public boolean isDone() {
        return true;
    }
}
