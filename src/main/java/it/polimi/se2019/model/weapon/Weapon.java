package it.polimi.se2019.model.weapon;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.serialization.CustomPrimaryEffectAdapter;
import it.polimi.se2019.util.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Weapon {
    private String mName;
    private AmmoValue mReloadCost;
    private AmmoValue mGrabCost;
    @Exclude private boolean mLoaded;

    // various effects
    @SerializedName("primary")
    @JsonAdapter(CustomPrimaryEffectAdapter.class)
    private PayedEffect mPrimaryEffect;
    @SerializedName("secondary") private PayedEffect mSecondaryEffect;
    @SerializedName("additional") private List<PayedEffect> mAdditionalEffects;

    // used in tests
    public Weapon () {}

    public Weapon (String name, AmmoValue reloadCost, AmmoValue grabCost) {
        if (reloadCost == null || grabCost == null) {
            throw new IllegalArgumentException("Can't use null ammoValues");
        }

        mName = name;
        mReloadCost = reloadCost;
        mGrabCost = grabCost;
        mLoaded = false;
    }

    // trivial getters
    public String getName() {
        return mName;
    }

    public AmmoValue getReloadCost() {
        return mReloadCost;
    }

    public AmmoValue getGrabCost() {
        return mGrabCost;
    }

    public boolean isLoaded() {
        return mLoaded;
    }

    // trivial setters
    public void setPrimaryEffect(Expression behaviour)  {
        // primary effect is always free
        mPrimaryEffect = new PayedEffect(new AmmoValue(), behaviour);
    }

    // TODO: add doc
    // TODO: implement
    public void setLoaded(boolean value) {
        mLoaded = value;
    }

    // TODO: add doc
    // TODO: implement
    public Weapon deepCopy() {
        throw new UnsupportedOperationException();
    }

    // TODO: add doc
    // TODO: implement
    public Action shoot(ShootContext shootContext) {
        return null;
    }

    public static List<Weapon> returnDeckFromJson(String json) {
        Gson gson = new Gson();
        Weapon[] weapons = gson.fromJson(json, Weapon[].class);

        return new ArrayList<>(Arrays.asList(weapons));
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "mName='" + mName + '\'' +
                ", mReloadCost=" + mReloadCost +
                ", mGrabCost=" + mGrabCost +
                '}';
    }
}