package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.model.Damage;

/**
 * A terminal expression containing a damage literal
 */
public class DamageLiteral extends Literal<Damage> {
    public DamageLiteral(Damage contents) {
        super(contents);
    }

    /**
     * Return internal damage value
     */
    @Override
    public Damage asDamage() {
        return getPrimitive();
    }
}
