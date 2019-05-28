package it.polimi.se2019.model.weapon.behaviour;

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
}
