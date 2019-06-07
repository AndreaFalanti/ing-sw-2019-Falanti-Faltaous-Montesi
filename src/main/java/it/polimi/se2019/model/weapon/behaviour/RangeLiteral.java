package it.polimi.se2019.model.weapon.behaviour;

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
}
