package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

public class GetTargets extends AtomicExpression {
    public GetTargets(AtomicExpression positions) {
        super();

        putSub("range", positions);
    }

    @Override
    protected AtomicExpression continueEval(ShootContext shootContext) {
        Set<Player> players = shootContext.getPlayers();
        Set<Position> positions = getSub("range").asRange();

        Set<PlayerColor> selectedPlayers = players.stream()
                .filter(pl -> positions.contains(pl.getPos()))
                .map(Player::getColor)
                .collect(Collectors.toSet());

        return new TargetsLiteral(selectedPlayers);
    }
}
