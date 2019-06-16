package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;

public class SelectOneTarget extends Behaviour {
    public SelectOneTarget() {
    }

    public SelectOneTarget(Expression from) {
        putSub("from", from);
    }

    @Override
    public Expression continueEval(ShootContext context) {
        // use generic expression to synthesise this
        Expression moreGeneric = new SelectTargets(
                new IntLiteral(1),
                new IntLiteral(1),
                getSub("from")
        );

        return moreGeneric.eval(context);
    }
}

