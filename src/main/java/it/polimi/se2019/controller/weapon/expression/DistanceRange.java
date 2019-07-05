package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Behaviour that evaluates to a range visible as a ring around a given origin.
 * The ring's internal and external radii, along with its origin, are specified as subexpressions.
 * @author Stefano Montesi
 */
public class DistanceRange extends Behaviour {
    private static final String ORIGIN = "origin";

    // required for Gson; should never be called by the user
    public DistanceRange() {
        putSub(ORIGIN, new Pos(new You()));
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     * @param origin the origin of the ring
     * @param min internal radius of the ring
     * @param max external radius of the ring
     */
    public DistanceRange(Expression origin, Expression min, Expression max) {
        putSub(ORIGIN, origin);
        putSub("min", min);
        putSub("max", max);
    }

    /**
     * Constructs the behaviour with using the given subexpressions
     *  NB. the origin is set to the shooter's position as default
     * @param min internal radius of the ring
     * @param max external radius of the ring
     */
    public DistanceRange(Expression min, Expression max) {
        putSub("min", min);
        putSub("max", max);
    }

    /**
     * Evaluates expression
     * @param context context used for evaluation
     * @return result of evaluation
     */
    @Override
    public final Expression eval(ShootContext context) {
        Board board = context.getBoard();

        Set<Position> reachablePositions = board.getReachablePositions(
                getSub(ORIGIN).eval(context).asPosition(),
                getSub("min").eval(context).asInt(),
                getSub("max").eval(context).asInt()
        );

        return new SetExpression(reachablePositions.stream()
                .map(PositionLiteral::new)
                .collect(Collectors.toSet()));
    }
}
