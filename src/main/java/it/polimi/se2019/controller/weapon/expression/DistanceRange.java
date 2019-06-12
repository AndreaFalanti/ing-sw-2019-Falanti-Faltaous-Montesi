package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

import java.util.Set;
import java.util.stream.Collectors;

public class DistanceRange extends Behaviour {
    public DistanceRange() {
        putSub("origin", new You());
    }

    public DistanceRange(Expression origin, Expression min, Expression max) {
        putSub("origin", origin);
        putSub("min", min);
        putSub("max", max);
    }

    public DistanceRange(Expression min, Expression max) {
        putSub("min", min);
        putSub("max", max);
    }

    // TODO: rephrase doc more succinctly...
    /**
     *
     * @param context used for board and shooter position
     * @return RangeLiteral describing a "circular halo" that encircles the shooter position.
     *         Said halo contains all positions that are a contained between a range of distances
     *         from the shooter (the range is described by a minimum and a maximum distance, which are set
     *         when {@code this}  is instantiated (see the constructor for more info)
     */
    @Override
    protected final Expression continueEval(ShootContext context) {
        Board board = context.getBoard();

        Set<Position> reachablePositions = board.getReachablePositions(
                getSub("origin").asPosition(),
                getSub("min").asInt(),
                getSub("max").asInt()
        );

        return SetExpression.from(reachablePositions.stream()
                .map(PositionLiteral::new)
                .collect(Collectors.toSet()));
    }
}
