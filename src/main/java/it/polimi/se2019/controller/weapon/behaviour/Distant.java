package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.controller.weapon.Expression;
import it.polimi.se2019.controller.weapon.ShootContext;

public class Distant extends Behaviour {
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
     * @param context used for board and shooter position
     * @return RangeLiteral describing a "circular halo" that encircles the shooter position.
     *         Said halo contains all positions that are a contained between a range of distances
     *         from the shooter (the range is described by a minimum and a maximum distance, which are set
     *         when {@code this}  is instantiated (see the constructor for more info)
     */
    @Override
    protected final Expression continueEval(ShootContext context) {
        Board board = context.getBoard();
        Position shooterPos = context.getShooterPosition();

        return new RangeLiteral(board.getReachablePositions(
                shooterPos,
                getSub("minDistance").asInt(),
                getSub("maxDistance").asInt()
        ));
    }
}

