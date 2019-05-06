package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;

import java.util.Optional;

public class DamageLiteral extends Literal<Damage> {
    public DamageLiteral(Damage contents) {
        super(contents);
    }

    @Override
    Damage asDamage() {
        return getPrimitive();
    }
}
