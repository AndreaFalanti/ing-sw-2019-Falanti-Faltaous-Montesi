package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

public class Distant implements Expression {
    int mMinDistance;
    int mMaxDistance;

    // trivial constructors
    public Distant(int exactDistance) {
        mMinDistance = exactDistance;
        mMaxDistance = exactDistance;
    }
    public Distant(int minDistance, int maxDistance) {
        mMinDistance = minDistance;
        mMaxDistance = maxDistance;
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
    public Expression eval(ShootContext shootContext) {
        Board board = shootContext.getBoard();
        Position shooterPos = shootContext.getShooterPosition();

        return new RangeLiteral(board.getReachablePositions(shooterPos, mMinDistance, mMaxDistance));
    }
}

// Expression primary = new Cons(
        // new Pay(new AmmoValue(1, 0, 0)),
        // new Hurt(
            // new DamageLiteral(1),
            // new PickOneTarget(
                    // new IntersectRanges(
                            // new FloodRange(2, FloodRange.INF),
                            // // new CanSee()
                    // )
            // )
        // )
// );


