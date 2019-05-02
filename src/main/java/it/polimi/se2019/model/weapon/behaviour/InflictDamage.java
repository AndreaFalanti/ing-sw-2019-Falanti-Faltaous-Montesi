package it.polimi.se2019.model.weapon.behaviour;

import it.polimi.se2019.model.Damage;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;
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
    public Expression eval(Context context) {
        Set<PlayerColor> targetColors = mGetTargetExpr.eval(context).asTargets();
        PlayerColor inflicterColor    = context.getShooterColor();

        return new DamageActionLiteral(new DamageAction(inflicterColor, targetColors, mDamageToInflict.asDamage()));
    }
}
