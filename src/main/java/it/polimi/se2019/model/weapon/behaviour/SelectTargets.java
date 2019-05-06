package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.request.TargetSelectionRequest;


public class SelectTargets extends Expression {
    Expression mTargetsToSelectFrom;
    Expression mMinNumToSelect;
    Expression mMaxNumToSelect;

    public SelectTargets(Expression minNumToSelect, Expression maxNumToSelect,
                         Expression targetsToSelectFrom) {
        super(targetsToSelectFrom, minNumToSelect, maxNumToSelect);

        mMinNumToSelect = minNumToSelect;
        mMaxNumToSelect = maxNumToSelect;
        mTargetsToSelectFrom = targetsToSelectFrom;
    }

    // TODO: add doc
    @Override
    public final Expression continueEval(ShootContext shootContext) {
        return new RequestLiteral(new TargetSelectionRequest(
                mMinNumToSelect.asInt(),
                mMaxNumToSelect.asInt(),
                mTargetsToSelectFrom.asTargets()
        ));
    }
}
