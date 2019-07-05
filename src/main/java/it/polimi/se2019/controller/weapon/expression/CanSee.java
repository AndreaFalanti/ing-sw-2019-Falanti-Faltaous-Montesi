package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Behaviour for returning all targets that a given observer can see
 *  NB. the observer is considered the shooter if not specified
 * @author Stefano Montesi
 */
public class CanSee extends Behaviour {
    private static final String ORIGIN = "origin";

    public CanSee() {
        putSub(ORIGIN, new You());
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param origin the origin of the visibility range to obtain
     */
    public CanSee(Expression origin) {
        putSub(ORIGIN, origin);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Set<Position> visibleRange = new GetVisibleRange(
                new Pos(getSub(ORIGIN).eval(context))
        ).eval(context).asRange();
        Set<Player> allPlayers = context.getPlayers();

        return new SetExpression(allPlayers.stream()
                .filter(pl -> visibleRange.contains(pl.getPos()))
                .map(Player::getColor)
                .map(TargetLiteral::new)
                .collect(Collectors.toSet()));
    }
}
