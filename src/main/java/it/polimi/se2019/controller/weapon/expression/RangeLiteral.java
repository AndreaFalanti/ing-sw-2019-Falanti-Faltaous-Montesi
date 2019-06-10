package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.Position;

import java.util.Set;

public class RangeLiteral extends Literal<Set<Position>> {
    public RangeLiteral(Set<Position> contents) {
        super(contents);
    }

    @Override
    public Set<Position> asRange() {
        return getPrimitive();
    }

    @Override
    public Position asPosition() {
        if (mContents.size() == 1)
            return mContents.iterator().next();

        throw new UnsupportedConversionException(getClass().getSimpleName(), "Position",
                "This RangeLiteral contains multiple positions!"
        );
    }
}
