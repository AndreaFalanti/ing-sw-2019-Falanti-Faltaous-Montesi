package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;

public class InflictDamage extends AtomicExpression {
    // trivial constructor
    public InflictDamage(AtomicExpression damageToInflict, AtomicExpression targets) {
        putSub("amount", damageToInflict);
        putSub("to", targets);
    }

    // TODO: add doc
    @Override
    protected final AtomicExpression continueEval(ShootContext shootContext) {
        PlayerColor inflicterColor = shootContext.getShooterColor();

        // calculate resulting action
        Action resultingAction = new DamageAction(
                inflicterColor,
                getSub("to").asTargets(),
                getSub("amount").asDamage()
        );
        // add it to actions produced this far
        shootContext.pushAction(resultingAction);
        // return it
        return new Done();
    }
}
