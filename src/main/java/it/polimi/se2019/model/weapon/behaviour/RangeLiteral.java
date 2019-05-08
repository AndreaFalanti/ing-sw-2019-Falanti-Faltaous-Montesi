package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.Selection;

import java.util.HashSet;
import java.util.Set;

public class RangeLiteral extends Literal<Selection<Position>> {
    public RangeLiteral(Selection<Position> contents) {
        super(contents);
    }
    // TODO: make use of Selection<Position> more pervasive instead of relying on this
    public RangeLiteral(Set<Position> contentsAsSet) {
        super(Selection.fromSet(contentsAsSet));
    }

    @Override
    Selection<Position> asRange() {
        return getPrimitive();
    }
}
