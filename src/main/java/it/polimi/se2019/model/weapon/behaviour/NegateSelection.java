package it.polimi.se2019.model.weapon.behaviour;

public class NegateSelection<T> extends Expression {
    @SubExpression Expression mSelection;

    public NegateSelection(Expression selection) {
        super();

        mSelection = selection;
    }

    @Override
    protected Expression continueEval(ShootContext shootContext) {
        return new SelectionLiteral(mSelection.asSelection().negate());
    }
}
