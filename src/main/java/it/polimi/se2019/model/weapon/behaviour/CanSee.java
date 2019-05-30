package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

public class CanSee extends Expression {
    public CanSee() {
        super();
    }

    // TODO: add doc
    @Override
    public Expression continueEval(ShootContext shootContext) {
        Set<Position> visibleRange = new GetVisibleRange().eval(shootContext).asRange();
        Set<Player> allPlayers = shootContext.getPlayers();

        return new TargetsLiteral(allPlayers.stream()
                .filter(pl -> visibleRange.contains(pl.getPos()))
                .map(Player::getColor)
                .collect(Collectors.toSet())
        );
    }
}
