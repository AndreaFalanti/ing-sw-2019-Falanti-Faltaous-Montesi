package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;

import java.util.Optional;

public class DamageLiteral implements Expression {
    Damage mContents;

    public DamageLiteral(Damage contents) {
        mContents = contents;
    }

    @Override
    public Expression eval(ShootContext shootContext) {
        return this;
    }

    @Override
    public Optional<Damage> asDamage() {
        return Optional.of(mContents);
    }
}
