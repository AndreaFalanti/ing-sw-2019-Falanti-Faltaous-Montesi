package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Expression for returning all targets standing in a given range
 * @author Stefano Montesi
 */
public class All extends Behaviour {
    // Required for Gson; should never be used
    public All() {

    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param from range of positions from which targets are selected
     */
    public All(Expression from) {
        putSub("from", from);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Set<Player> players = context.getPlayers();
        Set<Position> range = getSub("from").eval(context).asRange();

        return new SetExpression(players.stream()
                .filter(pl -> range.contains(pl.getPos()))
                .map(Player::getColor)
                .map(TargetLiteral::new)
                .collect(Collectors.toSet()));
    }
}
