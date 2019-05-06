package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.request.TargetSelectionRequest;


public class SelectTargets implements Expression {
    Expression mTargetsToSelectFrom;
    Expression mMinNumToSelect;
    Expression mMaxNumToSelect;

    public SelectTargets(Expression minNumToSelect, Expression maxNumToSelect,
                         Expression targetsToSelectFrom) {
        mMinNumToSelect = minNumToSelect;
        mMaxNumToSelect = maxNumToSelect;
        mTargetsToSelectFrom = targetsToSelectFrom;
    }

    @Override
    public Expression eval(ShootContext shootContext) {
        mMinNumToSelect = mMinNumToSelect.eval(shootContext);
        mMaxNumToSelect = mMaxNumToSelect.eval(shootContext);
        mTargetsToSelectFrom = mTargetsToSelectFrom.eval(shootContext);

        // check if needed info is available
        Expression selectedTarget = shootContext.popInfo();

        // if info is there, just return the selected target
        // TODO: check if expression type is indeed a TargetsLiteral
        if (selectedTarget != null)
            return selectedTarget;

        // if not, then return a matching info request
        return new RequestLiteral(new TargetSelectionRequest(
                mMinNumToSelect.asInt(),
                mMaxNumToSelect.asInt(),
                mTargetsToSelectFrom.asTargets()
        ));
    }

}
