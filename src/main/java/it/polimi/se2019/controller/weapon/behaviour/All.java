package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

public class All extends Behaviour {
    public All() {

    }

    public All(Expression from) {
        putSub("from", from);
    }

    @Override
    protected Expression continueEval(ShootContext context) {
        Set<Player> players = context.getPlayers();
        Set<Position> range = getSub("from").asRange();

        Set<PlayerColor> result = players.stream()
                .filter(pl -> range.contains(pl.getPos()))
                .map(Player::getColor)
                .collect(Collectors.toSet());

        return new TargetsLiteral(result);
    }
}
