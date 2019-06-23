package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;
import java.util.stream.Collectors;

public class NegateTargets extends Behaviour {
    public NegateTargets() {

    }

    public NegateTargets(Expression selection) {
        putSub("selection", selection);
    }

    @Override
    public final Expression eval(ShootContext context) {
        Set<PlayerColor> targets = getSub("selection").eval(context).asTargets();

        Set<PlayerColor> negatedTargets = context.getPlayers().stream()
                .map(Player::getColor)
                .filter(plColor -> !targets.contains(plColor))
                .collect(Collectors.toSet());

        return new SetExpression(negatedTargets.stream()
                .map(TargetLiteral::new)
                .collect(Collectors.toSet())
        );
    }
}
