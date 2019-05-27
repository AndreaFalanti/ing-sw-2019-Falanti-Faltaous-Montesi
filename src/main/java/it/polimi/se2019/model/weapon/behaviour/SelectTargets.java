package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.response.TargetSelectionResponse;
import it.polimi.se2019.view.request.weapon.ResponseLiteral;

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
        Expression infoToRequest = new ResponseLiteral(new TargetSelectionResponse(
                mMin.asInt(),
                mMax.asInt(),
                mFrom.asTargetSelection()
        ));

        return requestInfoFromPlayer(shootContext, infoToRequest);
    }
}
