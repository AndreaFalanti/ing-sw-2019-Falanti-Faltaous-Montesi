package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.board.Direction;

/**
 * Literal containing a direction
 * @author Stefano Montesi
 */
public class DirectionLiteral extends Literal<Direction> {
    public DirectionLiteral(Direction contents) {
        super(contents);
    }

    @Override
    public Direction asDirection() {
        return getPrimitive();
    }
}
