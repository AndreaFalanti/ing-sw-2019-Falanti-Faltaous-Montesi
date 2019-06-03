package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.view.View;

import java.util.Set;


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

        Set<PlayerColor> selectedTargets = view.selectTargets(
                getSub("min").asInt(),
                getSub("max").asInt(),
                getSub("from").asTargets()
        );

        return new TargetsLiteral(selectedTargets);
    }
}
