package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.behaviour.Behaviour;

/**
 * Used for storing information about effect priorities and costs
 */
public class Effect {
    // fields
    private int mPriority;
    private boolean mOptional;
    private AmmoValue mCost;
    private Behaviour mBehaviour;

    // trivial getters
    public int getPriority() {
        return mPriority;
    }

    public boolean isOptional() {
        return mOptional;
    }

    public AmmoValue getCost() {
        return mCost;
    }

    public Behaviour getBehaviour() {
        return mBehaviour;
    }
}

