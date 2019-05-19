package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.request.TargetSelectionRequest;

import java.util.Optional;


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
        // check if needed info is available
        Optional<Expression> neededInfo = shootContext.popCollectedInfo();

        // if not available, request it and wait for it
        if (!neededInfo.isPresent()) {
            shootContext.pushRequestedInfo(new RequestLiteral(new TargetSelectionRequest(
                    mMin.asInt(),
                    mMax.asInt(),
                    mFrom.asTargetSelection()
            )));

            return new WaitForInfo();
        }

        // else just return it
        // TODO: also should check it
        return neededInfo.get();
    }
}
