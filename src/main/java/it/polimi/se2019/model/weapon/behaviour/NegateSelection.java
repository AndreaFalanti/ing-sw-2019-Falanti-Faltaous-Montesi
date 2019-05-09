package it.polimi.se2019.model.weapon.behaviour;

public class NegateSelection<T> extends Expression {
    @SubExpression Expression mSelection;

    @Override
    protected Expression continueEval(ShootContext shootContext) {
        return new SelectionLiteral(mSelection.asSelection().negate());
    }
}
