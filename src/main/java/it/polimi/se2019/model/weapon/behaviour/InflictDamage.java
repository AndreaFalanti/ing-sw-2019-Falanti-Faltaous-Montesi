package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Expression;

public class InflictDamage extends Behaviour {
    // trivial constructor
    public InflictDamage(Expression damageToInflict, Expression targets) {
        putSub("amount", damageToInflict);
        putSub("to", targets);
    }

    // TODO: add doc
    @Override
    protected final Expression continueEval(ShootContext context) {
        PlayerColor inflicterColor = context.getShooterColor();

        // calculate resulting action
        inflictDamage(
                inflicterColor,
                getSub("to").asTargets(),
                getSub("amount").asDamage()
        );

        // return it
        return new Done();
    }
}
