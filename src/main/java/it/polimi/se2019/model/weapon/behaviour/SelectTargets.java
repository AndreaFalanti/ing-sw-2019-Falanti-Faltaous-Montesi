package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.request.TargetSelectionRequest;


public class SelectTargets extends Expression {
    @SubExpression Expression mFrom;
    @SubExpression Expression mMin;
    @SubExpression Expression mMax;

    public SelectTargets(Expression minNumToSelect, Expression maxNumToSelect,
                         Expression targetsToSelectFrom) {
        super();

        mMin = minNumToSelect;
        mMax = maxNumToSelect;
        mFrom = targetsToSelectFrom;
    }

    // TODO: add doc
    @Override
    public final Expression continueEval(ShootContext shootContext) {
        return new RequestLiteral(new TargetSelectionRequest(
                mMin.asInt(),
                mMax.asInt(),
                mFrom.asTargetSelection()
        ));
    }
}
