package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.util.StringUtils;

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

    // expression
    private Expression mBehaviour;

    // trivial getters
    public String getId() {
        return mId != null ? mId : StringUtils.toSnakeCase(mName);
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

    public Expression getBehaviour() {
        return mBehaviour;
    }
}

