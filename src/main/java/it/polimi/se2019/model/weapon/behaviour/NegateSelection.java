package it.polimi.se2019.model.weapon.behaviour;

public class NegateSelection extends Expression {
    @SubExpression Expression mDo;

    public NegateSelection(Expression selection) {
        super();

        mDo = selection;
    }

    @Override
    protected Expression continueEval(ShootContext shootContext) {
        return new SelectionLiteral(mDo.asSelection().negate());
    }
}
