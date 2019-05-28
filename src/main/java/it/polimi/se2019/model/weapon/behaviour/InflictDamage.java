package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;

public class InflictDamage extends Expression {
    // trivial constructor
    public InflictDamage(Expression damageToInflict, Expression targets) {
        putSub("damage", damageToInflict);
        putSub("to", targets);
    }

    // TODO: add doc
    @Override
    protected final Expression continueEval(ShootContext shootContext) {
        PlayerColor inflicterColor = shootContext.getShooterColor();

        // calculate resulting action
        Action resultingAction = new DamageAction(
                inflicterColor,
                getSub("to").asTargets(),
                getSub("damage").asDamage()
        );
        // add it to actions produced this far
        shootContext.pushAction(resultingAction);
        // return it
        return new Done();
    }
}
