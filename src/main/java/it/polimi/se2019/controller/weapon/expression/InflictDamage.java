package it.polimi.se2019.controller.weapon.expression;

import it.polimi.se2019.controller.weapon.ShootContext;
import it.polimi.se2019.model.PlayerColor;

public class InflictDamage extends Behaviour {
    public InflictDamage() {

    }

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
                context,
                inflicterColor,
                getSub("to").asTargets(),
                getSub("amount").asDamage()
        );

        // return it
        return new Done();
    }
}
