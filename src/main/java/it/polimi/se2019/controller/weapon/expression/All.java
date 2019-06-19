package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
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
    public final Expression eval(ShootContext context) {
        Set<Player> players = context.getPlayers();
        Set<Position> range = getSub("from").eval(context).asRange();

        return SetExpression.from(players.stream()
                .filter(pl -> range.contains(pl.getPos()))
                .map(Player::getColor)
                .map(TargetLiteral::new)
                .collect(Collectors.toSet()));
    }
}
