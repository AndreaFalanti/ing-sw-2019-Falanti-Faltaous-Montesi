package it.polimi.se2019.model.weapon.behaviour;

public class SelectOneTarget extends Expression {
    public SelectOneTarget(Expression from) {
        super();

        putSub("from", from);
    }

    @Override
    public Expression continueEval(ShootContext shootContext) {
        // use generic expression to synthesise this
        Expression moreGeneric = new SelectTargets(
                new IntLiteral(1),
                new IntLiteral(1),
                getSub("from")
        );

        return moreGeneric.eval(shootContext);
    }
}

