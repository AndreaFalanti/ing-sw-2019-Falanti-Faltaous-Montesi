package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.Selection;

public class SelectionLiteral<T> extends Literal<Selection<T>> {
    public SelectionLiteral(Selection<T> contents) {
        super(contents);
    }

    @Override
    public Selection<?> asSelection() { return getPrimitive(); }

    @Override
    public Selection<PlayerColor> asTargetSelection() { return (Selection<PlayerColor>) getPrimitive(); }

    @Override
    public Selection<Position> asRange() { return (Selection<Position>) getPrimitive(); }
}

