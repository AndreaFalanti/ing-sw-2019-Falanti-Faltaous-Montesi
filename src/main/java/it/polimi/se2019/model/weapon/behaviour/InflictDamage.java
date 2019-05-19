package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.DamageAction;

import java.util.Set;

public class InflictDamage extends Expression {
    // subexpressions
    private @SubExpression Expression mTargets;
    private @SubExpression Expression mDamage;

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

        // calculate resulting action
        Action resultingAction = new DamageAction(
                inflicterColor,
                mTargets.asTargetSelection().asSet(),
                mDamage.asDamage()
        );
        // add it to actions produced this far
        shootContext.pushAction(resultingAction);
        // return it
        return new ActionLiteral(resultingAction);
    }
}
