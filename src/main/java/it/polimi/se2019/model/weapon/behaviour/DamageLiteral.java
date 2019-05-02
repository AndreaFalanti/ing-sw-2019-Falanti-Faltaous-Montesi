package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;

public class DamageLiteral implements Expression {
    Damage mContents;

    public DamageLiteral(Damage contents) {
        mContents = contents;
    }

    @Override
    public Expression eval(Context context) {
        return this;
    }

    @Override
    public Damage asDamage() {
        return mContents;
    }
}
