package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.board.TileColor;

public class ColorLiteral extends Literal<TileColor> {
    public ColorLiteral(TileColor contents) {
        super(contents);
    }

    @Override
    public TileColor asColor() {
        return getPrimitive();
    }
}
