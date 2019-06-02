package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

public class Distant extends Expression {
    // trivial constructors
    public Distant(Expression minDistance, Expression maxDistance) {
        putSub("minDistance", minDistance);
        putSub("maxDistance", maxDistance);
    }
    public Distant(Expression exactDistance) {
        putSub("minDistance", exactDistance);
        putSub("maxDistance", exactDistance);
    }

    // TODO: rephrase doc more succinctly...
    /**
     *
     * @param shootContext used for board and shooter position
     * @return RangeLiteral describing a "circular halo" that encircles the shooter position.
     *         Said halo contains all positions that are a contained between a range of distances
     *         from the shooter (the range is described by a minimum and a maximum distance, which are set
     *         when {@code this}  is instantiated (see the constructor for more info)
     */
    @Override
    protected final Expression continueEval(ShootContext shootContext) {
        Board board = shootContext.getBoard();
        Position shooterPos = shootContext.getShooterPosition();

        return new RangeLiteral(board.getReachablePositions(
                shooterPos,
                getSub("minDistance").asInt(),
                getSub("maxDistance").asInt()
        ));
    }
}

