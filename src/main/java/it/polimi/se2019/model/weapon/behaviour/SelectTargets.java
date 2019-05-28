package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.response.TargetSelectionResponse;
import it.polimi.se2019.view.request.weapon.ResponseLiteral;


public class SelectTargets extends Expression {
    public SelectTargets(Expression min, Expression max, Expression from) {
        super();

        putSub("min", min);
        putSub("max", max);
        putSub("from", from);
    }

    // TODO: add doc
    @Override
    public final Expression continueEval(ShootContext shootContext) {
        Expression infoToRequest = new ResponseLiteral(new TargetSelectionResponse(
                getSub("min").asInt(),
                getSub("max").asInt(),
                getSub("from").asTargets()
        ));

        return requestInfoFromPlayer(shootContext, infoToRequest);
    }
}
