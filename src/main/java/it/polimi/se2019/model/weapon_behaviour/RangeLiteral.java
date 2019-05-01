package it.polimi.se2019.model.weapon_behaviour;

import it.polimi.se2019.model.Position;

import java.util.HashSet;
import java.util.Set;

public class RangeLiteral extends Expression {
    final Set<Position> mPositionsInRange = new HashSet<>();

    public RangeLiteral(Set<Position> mPositionsInRange) {
        mPositionsInRange.addAll(mPositionsInRange);
    }

    @Override
    public Expression eval(Context context) {
        return this;
    }
}
