package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;
import it.polimi.se2019.view.View;


public class SelectTargets extends Behaviour {
    public SelectTargets(Expression min, Expression max, Expression from) {
        super();

        putSub("min", min);
        putSub("max", max);
        putSub("from", from);
    }

    // TODO: add doc
    @Override
    public final Expression continueEval(ShootContext context) {
        View view = context.getView();

        // select targets
        Expression selectedTargets = new TargetsLiteral(view.selectTargets(
                getSub("min").asInt(),
                getSub("max").asInt(),
                getSub("from").asTargets()
        ));

        // save them into a variable
        context.setVar(LAST_SELECTED_TARGETS_VAR, selectedTargets.deepCopy());

        return selectedTargets;
    }
}
