package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;

import java.util.Set;
import java.util.stream.Collectors;

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

        // remove shooter from selectable targets
        Set<PlayerColor> targets = getSub("from").asTargets().stream()
                .filter(clr -> !clr.equals(context.getShooterColor()))
                .collect(Collectors.toSet());

        // select targets
        Set<PlayerColor> selectedTargets = view.selectTargets(
                getSub("min").asInt(),
                getSub("max").asInt(),
                targets
        );
        Expression result = SetExpression.from(selectedTargets.stream()
                .map(TargetLiteral::new)
                .collect(Collectors.toSet())
        );

        // save them into a variable
        // TODO: add this back
        // context.setVar(SPECIAL_VAR_LAST_SELECTED, result.deepCopy());

        return result;
    }
}
