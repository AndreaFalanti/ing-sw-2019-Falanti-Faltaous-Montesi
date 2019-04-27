package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;

public enum Direction {
    NORTH(new Position(0, -1)),
    EAST(new Position(1, 0)),
    SOUTH(new Position(0, 1)),
    WEST(new Position(-1, 0));

    Position mPosition;

    Direction(Position position) {
        mPosition = position;
    }

    Position toPosition() {
        return mPosition;
    }
}
