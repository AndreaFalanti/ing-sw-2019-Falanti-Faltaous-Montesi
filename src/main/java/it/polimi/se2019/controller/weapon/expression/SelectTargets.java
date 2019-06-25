package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;

import java.util.Set;
import java.util.logging.Level;
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

    private static Expression returnSelection(ShootContext context, Expression selection) {
        // first record selection in dedicated variable
        context.setVar(SPECIAL_VAR_LAST_SELECTED, selection.deepCopy());

        return selection;
    }

    // TODO: add doc
    @Override
    public final Expression eval(ShootContext context) {
        View view = context.getView();
        ShootInteraction interaction = context.getInteraction();

        // remove shooter from selectable targets
        Set<PlayerColor> targets = getSub("from").eval(context).asTargets().stream()
                .filter(clr -> !clr.equals(context.getShooterColor()))
                .collect(Collectors.toSet());

        // select targets
        Set<PlayerColor> selectedTargets = interaction.selectTargets(
                view,
                getSub("min").eval(context).asInt(),
                getSub("max").eval(context).asInt(),
                targets
        );

        LOGGER.log(Level.INFO, context.getShooterColor() + " selected " + selectedTargets + " among " + targets);

        Expression result = new SetExpression(selectedTargets.stream()
                .map(TargetLiteral::new)
                .collect(Collectors.toSet())
        );

        return returnSelection(context, result);
    }
}
