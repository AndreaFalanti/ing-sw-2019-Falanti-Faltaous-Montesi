package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.board.TileColor;

/**
 * A terminal expression containing a color value
 * @author Stefano Montesi
 */
public class ColorLiteral extends Literal<TileColor> {
    public ColorLiteral(TileColor contents) {
        super(contents);
    }

    /**
     * The expression as a color 
     */
    @Override
    public TileColor asColor() {
        return getPrimitive();
    }
}
