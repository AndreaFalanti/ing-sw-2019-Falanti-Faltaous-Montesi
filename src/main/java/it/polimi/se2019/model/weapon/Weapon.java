package it.polimi.se2019.model.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Weapon {
    private String mName;
    private AmmoValue mReloadCost;
    private AmmoValue mGrabCost;
    private boolean mLoaded;

    // behaviour of weapon used to shoot
    Expression mBehaviour;

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

    // trivial constructor
    public Weapon(Expression behaviour) {
        mBehaviour = behaviour;
    }

    //region GETTERS
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
    //endregion

    // TODO: add doc
    // TODO: implement
    public void setLoaded(boolean loaded) {
        ;
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