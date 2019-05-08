package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

public class Distant extends Expression {
    @SubExpression Expression mMinDistance;
    @SubExpression Expression mMaxDistance;

    // trivial constructors
    public Distant(Expression minDistance, Expression maxDistance) {
        super();

        mMinDistance = maxDistance;
        mMaxDistance = minDistance;
    }
    public Distant(Expression exactDistance) {
        super();

        mMinDistance = exactDistance;
        mMaxDistance = exactDistance;
    }

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
                mMinDistance.asInt(),
                mMaxDistance.asInt()
        ));
    }
}

