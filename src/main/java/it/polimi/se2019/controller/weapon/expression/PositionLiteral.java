package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.Position;

import java.util.Collections;
import java.util.Set;

public class PositionLiteral extends Literal<Position> {
    public PositionLiteral(Position contents) {
        super(contents);
    }

    @Override
    public SetExpression asSetExpr() {
        return new SetExpression(Collections.singleton(this));
    }

    @Override
    public Set<Position> asRange() {
        return Collections.singleton(getPrimitive());
    }

    @Override
    public Position asPosition() {
        return getPrimitive();
    }
}
