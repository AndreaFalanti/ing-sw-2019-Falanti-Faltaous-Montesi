package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;

import java.util.Optional;
import java.util.Set;

public class TargetsLiteral implements Expression {
    Set<PlayerColor> mContents;

    public TargetsLiteral(Set<PlayerColor> contents) {
        mContents = contents;
    }

    @Override
    public Expression eval(ShootContext shootContext) {
        return null;
    }

    @Override
    public Optional<Set<PlayerColor>> asTargets() {
        return Optional.of(mContents);
    }
}
