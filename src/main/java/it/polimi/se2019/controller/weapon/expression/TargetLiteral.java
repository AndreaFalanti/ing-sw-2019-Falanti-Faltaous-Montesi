package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.PlayerColor;

public class TargetLiteral extends Literal<PlayerColor> {
    public TargetLiteral(PlayerColor contents) {
        super(contents);
    }

    @Override
    public PlayerColor asTarget() {
        return getPrimitive();
    }
}
