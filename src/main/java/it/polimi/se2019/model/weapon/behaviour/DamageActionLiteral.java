package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.action.DamageAction;

public class DamageActionLiteral implements Expression {
    DamageAction mContents;

    public DamageActionLiteral(DamageAction contents) {
        mContents = contents;
    }

    @Override
    public Expression eval(ShootContext shootContext) {
        return this;
    }
}
