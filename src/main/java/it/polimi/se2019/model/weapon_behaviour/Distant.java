package it.polimi.se2019.model.weapon_behaviour;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;

public class Distant extends Expression {
    int mMinDistance;
    int mMaxDistance;

    public Distant(int exactDistance) {
        mMinDistance = exactDistance;
        mMaxDistance = exactDistance;
    }
    public Distant(int minDistance, int maxDistance) {
        mMinDistance = minDistance;
        mMaxDistance = maxDistance;
    }

    @Override
    public Expression eval(Context context) {
        Board board = context.getBoard();
        Position shooterPos = context.getShooterPosition();

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


