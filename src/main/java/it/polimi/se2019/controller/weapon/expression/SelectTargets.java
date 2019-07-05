package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.controller.weapon.ShootInteraction;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;

import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_LAST_SELECTED;
import static it.polimi.se2019.controller.weapon.ShootContext.SPECIAL_VAR_PREVIOUSLY_SELECTED;


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
        context.setVar(
                SPECIAL_VAR_PREVIOUSLY_SELECTED,
                new Union(
                        context.getVar(SPECIAL_VAR_LAST_SELECTED),
                        selection.deepCopy()
                ).eval(context)
        );

        return selection;
    }

    // TODO: add doc
    @Override
    public final Expression eval(ShootContext context) {
        View view = context.getView();
        ShootInteraction interaction = context.getInteraction();
        Game game = context.getGame();

        // remove players that cannot be selected
        Set<PlayerColor> targets = getSub("from").eval(context).asTargets().stream()
                // remove shooter from selectable targets
                .filter(clr -> !clr.equals(context.getShooterColor()))
                // remove not yet spawned players from selectable targets
                .filter(clr -> game.getPlayerFromColor(clr).isSpawned())
                // collect
                .collect(Collectors.toSet());

        // select targets
        Set<PlayerColor> selectedTargets = interaction.selectTargets(
                view,
                getSub("min").eval(context).asInt(),
                getSub("max").eval(context).asInt(),
                targets
        );

        LOGGER.log(Level.INFO, "{0} selected {1} among {2}",
                new Object[] {context.getShooterColor(), selectedTargets, targets});

        Expression result = new SetExpression(selectedTargets.stream()
                .map(TargetLiteral::new)
                .collect(Collectors.toSet())
        );

        return returnSelection(context, result);
    }
}
