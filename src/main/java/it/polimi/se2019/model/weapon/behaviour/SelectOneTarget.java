package it.polimi.se2019.model.weapon.behaviour;

public class SelectOneTarget extends Expression {
    private @SubExpression Expression mFrom;

    public SelectOneTarget(Expression from) {
        super();

        mFrom = from;
    }

    @Override
    public Expression continueEval(ShootContext shootContext) {
        // use generic expression to synthesise this
        Expression moreGeneric = new SelectTargets(
                new IntLiteral(1),
                new IntLiteral(1),
                mFrom
        );

        return moreGeneric.eval(shootContext);
    }
}

