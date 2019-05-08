package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Selection;

public class TargetsLiteral extends Literal<Selection<PlayerColor>> {
    public TargetsLiteral(Selection<PlayerColor> contents) {
        super(contents);
    }

    @Override
    Selection<PlayerColor> asTargetSelection() {
        return getPrimitive();
    }
}
