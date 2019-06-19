package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

public class CanSee extends Behaviour {
    public CanSee() {
        putSub("origin", new You());
    }

    public CanSee(Expression origin) {
        putSub("origin", origin);
    }

    // TODO: add doc
    @Override
    public final Expression eval(ShootContext context) {
        Set<Position> visibleRange = new GetVisibleRange(
                new Pos(getSub("origin").eval(context))
        ).eval(context).asRange();
        Set<Player> allPlayers = context.getPlayers();

        return SetExpression.from(allPlayers.stream()
                .filter(pl -> visibleRange.contains(pl.getPos()))
                .map(Player::getColor)
                .map(TargetLiteral::new)
                .collect(Collectors.toSet()));
    }
}
