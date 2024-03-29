package it.polimi.se2019.controller.weapon;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.util.Exclude;
import it.polimi.se2019.util.StringUtils;

import java.util.Objects;

/**
 * Used for storing information about effect priorities and costs
 * @author Stefano Montesi
 */
public class Effect {
    // unique identifier
    private String mId;

    // meta information
    private String mName;
    private Integer mPriority;
    private Boolean mOptional;
    private AmmoValue mCost;

    @Exclude
    private Expression mBehaviour;

    // trivial constructor
    public Effect(String name, int priority, boolean optional, AmmoValue cost) {
        mName = name;
        mPriority = priority;
        mOptional = optional;
        mCost = cost;
    }

    // trivial getters
    public String getId() {
        return mId != null ? mId : StringUtils.toSnakeCase(mName);
    }

    public String getName() {
        if (mName == null)
            throw new IllegalStateException("Effects must have a name!");

        return mName;
    }

    public int getPriority() {
        return mPriority == null ? 0 : mPriority;
    }

    public boolean isOptional() {
        return mOptional == null ? false : mOptional;
    }

    public AmmoValue getCost() {
        return mCost != null ? mCost : new AmmoValue(0, 0, 0);
    }

    public Expression getBehaviour() {
        return mBehaviour;
    }

    // trivial setters
    public void setOptional(boolean optional) {
        mOptional = optional;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Effect effect = (Effect) o;
        return Objects.equals(mPriority, effect.mPriority) &&
                Objects.equals(mOptional, effect.mOptional) &&
                Objects.equals(mId, effect.mId) &&
                Objects.equals(mName, effect.mName) &&
                Objects.equals(mCost, effect.mCost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mName, mPriority, mOptional, mCost);
    }
}

