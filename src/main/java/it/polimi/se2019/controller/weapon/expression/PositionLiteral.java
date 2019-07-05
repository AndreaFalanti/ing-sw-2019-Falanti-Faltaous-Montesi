package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.Position;

import java.util.Collections;
import java.util.Set;

/**
 * Literal containing a Position value
 * @author Stefano Montesi
 */
public class PositionLiteral extends Literal<Position> {
    public PositionLiteral(Position contents) {
        super(contents);
    }

    /**
     * @return literal as a SetExpression
     */
    @Override
    public SetExpression asSetExpr() {
        return new SetExpression(Collections.singleton(this));
    }

    /**
     * @return literal as range
     */
    @Override
    public Set<Position> asRange() {
        return Collections.singleton(getPrimitive());
    }

    /**
     * @return literal as a single position
     */
    @Override
    public Position asPosition() {
        return getPrimitive();
    }
}
