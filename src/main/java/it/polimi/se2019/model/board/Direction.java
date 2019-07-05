package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;

public enum Direction {
    NORTH(new Position(0, -1)),
    EAST(new Position(1, 0)),
    SOUTH(new Position(0, 1)),
    WEST(new Position(-1, 0));

    Position mPosition;

    public static Direction connectingDirection(Position from, Position to) {
        Position dist = from.subtract(to);

        if (dist.equals(new Position(0, -1)))
            return SOUTH;
        else if(dist.equals(new Position(1, 0)))
            return WEST;
        else if(dist.equals(new Position(0, 1)))
            return NORTH;
        else if(dist.equals(new Position(-1, 0)))
            return EAST;
        else
            throw new IllegalArgumentException("Positions cannot be connected by direction!");
    }

    Direction(Position position) {
        mPosition = position;
    }

    public Position toPosition() {
        return mPosition;
    }
}
