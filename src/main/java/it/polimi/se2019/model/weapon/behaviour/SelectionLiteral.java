package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.weapon.Selection;

public class SelectionLiteral<T> extends Literal<Selection<T>> {
    public SelectionLiteral(Selection<T> contents) {
        super(contents);
    }

    @Override
    public Selection<T> asSelection() {
        return getPrimitive();
    }
}
