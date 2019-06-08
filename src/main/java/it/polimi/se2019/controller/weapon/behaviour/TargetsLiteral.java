package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;

import java.util.Set;

public class TargetsLiteral extends Literal<Set<PlayerColor>> {
    public TargetsLiteral(Set<PlayerColor> contents) {
        super(contents);
    }

    @Override
    public Set<PlayerColor> asTargets() {
        return getPrimitive();
    }

    @Override
    public PlayerColor asTarget() {
        Set<PlayerColor> primitive = getPrimitive();

        if (primitive.size() == 1)
            return primitive.iterator().next();

        throw new UnsupportedConversionException(getClass().getSimpleName(), "Target",
                "This TargetsLiteral contains multiple targets!"
        );
    }
}
