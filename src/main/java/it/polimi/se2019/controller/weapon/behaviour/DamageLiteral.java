package it.polimi.se2019.controller.weapon.behaviour;

import it.polimi.se2019.model.Damage;

public class DamageLiteral extends Literal<Damage> {
    public DamageLiteral(Damage contents) {
        super(contents);
    }

    @Override
    public Damage asDamage() {
        return getPrimitive();
    }
}
