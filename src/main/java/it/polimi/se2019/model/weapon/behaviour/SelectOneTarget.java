package it.polimi.se2019.model.weapon.behaviour;

public class SelectOneTarget extends AtomicExpression {
    public SelectOneTarget(AtomicExpression from) {
        super();

        putSub("from", from);
    }

    @Override
    public AtomicExpression continueEval(ShootContext shootContext) {
        // use generic expression to synthesise this
        AtomicExpression moreGeneric = new SelectTargets(
                new IntLiteral(1),
                new IntLiteral(1),
                getSub("from")
        );

        return moreGeneric.eval(shootContext);
    }
}

