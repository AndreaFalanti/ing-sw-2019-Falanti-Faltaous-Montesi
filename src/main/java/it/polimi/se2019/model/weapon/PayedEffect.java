package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.behaviour.AtomicExpression;

public class PayedEffect {
    private AmmoValue mCost;
    private AtomicExpression mBehaviour;

    // trivial constructor
    public PayedEffect(AmmoValue cost, AtomicExpression behaviour) {
        mCost = cost;
        mBehaviour = behaviour;
    }

    // trivial getters
    public AmmoValue getCost() {
        return mCost;
    }

    public AtomicExpression getBehaviour() {
        return mBehaviour;
    }
}
