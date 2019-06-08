package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

import java.util.Set;
import java.util.stream.Collectors;

public class CanSee extends Behaviour {
    public static String OBSERVER = "observer";

    public CanSee() {
        putSub(OBSERVER, new You());
    }

    public CanSee(Expression observer) {
        putSub(OBSERVER, observer);
    }

    // TODO: add doc
    @Override
    public Expression continueEval(ShootContext context) {
        Set<Position> visibleRange = new GetVisibleRange(
                getSub(OBSERVER)
        ).eval(context).asRange();
        Set<Player> allPlayers = context.getPlayers();

        return new TargetsLiteral(allPlayers.stream()
                .filter(pl -> visibleRange.contains(pl.getPos()))
                .map(Player::getColor)
                .collect(Collectors.toSet())
        );
    }
}
