package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.Position;

public class PositionLiteral extends Literal<Position> {
    public PositionLiteral(Position contents) {
        super(contents);
    }

    @Override
    public Position asPosition() {
        return getPrimitive();
    }
}
