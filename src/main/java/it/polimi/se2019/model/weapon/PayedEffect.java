package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.behaviour.Behaviour;

public class PayedEffect {
    private AmmoValue mCost;
    private Behaviour mBehaviour;

    // trivial constructor
    public PayedEffect(AmmoValue cost, Behaviour behaviour) {
        mCost = cost;
        mBehaviour = behaviour;
    }

    // trivial getters
    public AmmoValue getCost() {
        return mCost;
    }

    public Behaviour getBehaviour() {
        return mBehaviour;
    }
}
