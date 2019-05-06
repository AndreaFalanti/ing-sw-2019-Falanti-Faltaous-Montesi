package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.DamageAction;

import java.util.Set;

public class InflictDamage implements Expression {
    // subexpressions
    Expression mGetTargetExpr;
    Expression mDamageToInflict;


    // trivial constructor
    public InflictDamage(Expression damageToInflict, Expression getTargetExpr) {
        mDamageToInflict = damageToInflict;
        mGetTargetExpr   = getTargetExpr;
    }

    // TODO: add doc
    @Override
    public Expression eval(ShootContext shootContext) {
        Set<PlayerColor> targetColors = mGetTargetExpr.eval(shootContext).asTargets();
        PlayerColor inflicterColor    = shootContext.getShooterColor();

        return new DamageActionLiteral(new DamageAction(inflicterColor, targetColors, mDamageToInflict.asDamage()));
    }
}
