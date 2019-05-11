package it.polimi.se2019.model.weapon.behaviour;

public class Done extends Expression {
    @Override
    protected Expression continueEval(ShootContext shootContext) {
        return this;
    }
}
