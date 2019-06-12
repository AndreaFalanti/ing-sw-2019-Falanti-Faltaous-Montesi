package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;
import java.util.stream.Collectors;

public class NegateSelection extends Behaviour {
    public NegateSelection() {

    }

    public NegateSelection(Expression selection) {
        putSub("selection", selection);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Set<PlayerColor> targets = getSub("selection").asTargets();

        Set<PlayerColor> negatedTargets = context.getPlayers().stream()
                .map(Player::getColor)
                .filter(plColor -> !targets.contains(plColor))
                .collect(Collectors.toSet());

        return SetExpression.from(negatedTargets.stream()
                .map(TargetLiteral::new)
                .collect(Collectors.toSet())
        );
    }
}
