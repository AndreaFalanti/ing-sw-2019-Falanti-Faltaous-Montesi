package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;
import java.util.stream.Collectors;

public class NegateSelection extends Expression {
    public NegateSelection(Expression selection) {
        putSub("selection", selection);
    }

    @Override
    protected Expression continueEval(ShootContext shootContext) {
        Set<PlayerColor> targets = getSub("selection").asTargets();

        Set<PlayerColor> negatedTargets = shootContext.getPlayers().stream()
                .map(Player::getColor)
                .filter(plColor -> !targets.contains(plColor))
                .collect(Collectors.toSet());

        return new TargetsLiteral(negatedTargets);
    }
}
