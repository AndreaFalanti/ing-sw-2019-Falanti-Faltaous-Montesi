package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.Expression;
import it.polimi.se2019.model.weapon.ShootContext;

import java.util.Set;
import java.util.stream.Collectors;

public class CanSee extends Behaviour {
    public CanSee() {
    }

    public CanSee(Expression observer) {
        putSub("observer", observer);
    }

    // TODO: add doc
    @Override
    protected final Expression handleSubDefaultValue(String subName, ShootContext context) {
        if (subName.equals("observer")) {
            return new You();
        }

        return super.handleSubDefaultValue(subName, context);
    }

    // TODO: add doc
    @Override
    public Expression continueEval(ShootContext context) {
        Set<Position> visibleRange = new GetVisibleRange(
                getSub("observer")
        ).eval(context).asRange();
        Set<Player> allPlayers = context.getPlayers();

        return new TargetsLiteral(allPlayers.stream()
                .filter(pl -> visibleRange.contains(pl.getPos()))
                .map(Player::getColor)
                .collect(Collectors.toSet())
        );
    }
}
