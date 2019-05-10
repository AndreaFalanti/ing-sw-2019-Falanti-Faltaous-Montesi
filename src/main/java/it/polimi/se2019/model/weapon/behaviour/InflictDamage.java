package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.DamageAction;

import java.util.Set;

public class InflictDamage extends Expression {
    // subexpressions
    @SubExpression Expression mTargets;
    @SubExpression Expression mDamage;

    // trivial constructor
    public InflictDamage(Expression damageToInflict, Expression targets) {
        super();

        mDamage = damageToInflict;
        mTargets = targets;
    }

    // TODO: add doc
    @Override
    protected final Expression continueEval(ShootContext shootContext) {
        PlayerColor inflicterColor = shootContext.getShooterColor();

        return new ActionLiteral(new DamageAction(
                inflicterColor,
                mTargets.asTargetSelection().asSet(),
                mDamage.asDamage()
        ));
    }
}
