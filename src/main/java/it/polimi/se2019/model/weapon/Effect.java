package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.behaviour.Behaviour;
import it.polimi.se2019.util.Exclude;

/**
 * Used for storing information about effect priorities and costs
 */
public class Effect {
    // unique identifier
    private String mId;

    // meta information
    private String mName;
    private int mPriority;
    private boolean mOptional;
    private AmmoValue mCost;

    // behaviour
    private Behaviour mBehaviour;

    // trivial getters
    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

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

