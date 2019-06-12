package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.view.View;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_LAST_SELECTED;


public class SelectTargets extends Behaviour {
    public SelectTargets() {
    }

    public SelectTargets(Expression min, Expression max, Expression from) {
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
        // TODO: add this back
        // context.setVar(SPECIAL_VAR_LAST_SELECTED, selectedTargets.deepCopy());

        return selectedTargets;
    }
}
