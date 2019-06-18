package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.PlayerColor;

import java.util.Collections;
import java.util.Set;

public class TargetLiteral extends Literal<PlayerColor> {
    public TargetLiteral(PlayerColor contents) {
        super(contents);
    }

    @Override
    public Set<PlayerColor> asTargets() {
        return Collections.singleton(getPrimitive());
    }

    @Override
    public PlayerColor asTarget() {
        return getPrimitive();
    }
}
