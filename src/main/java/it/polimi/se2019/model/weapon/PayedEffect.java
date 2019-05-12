package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.behaviour.Expression;

public class PayedEffect {
    private AmmoValue mCost;
    private Expression mBehaviour;

    // trivial constructor
    public PayedEffect(AmmoValue cost, Expression behaviour) {
        mCost = cost;
        mBehaviour = behaviour;
    }

    // trivial getters
    public AmmoValue getCost() {
        return mCost;
    }

    public Expression getBehaviour() {
        return mBehaviour;
    }
}
