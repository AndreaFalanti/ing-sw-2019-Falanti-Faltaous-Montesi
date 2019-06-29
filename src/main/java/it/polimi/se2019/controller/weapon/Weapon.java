package it.polimi.se2019.controller.weapon;

import com.google.gson.Gson;
import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.controller.weapon.expression.Behaviour;
import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Weapon {
    private String mName;
    private AmmoValue mReloadCost;
    private AmmoValue mGrabCost;
    private String mGuiID;

    private boolean mLoaded;

    // weapon expression
    @Exclude
    private Expression mBehaviour;

    // used in tests
    public Weapon() {}

    public Weapon(String name, AmmoValue reloadCost, AmmoValue grabCost) {
        if (reloadCost == null || grabCost == null) {
            throw new IllegalArgumentException("Can't use null ammoValues");
        }

        mName = name;
        mReloadCost = reloadCost;
        mGrabCost = grabCost;
        mLoaded = false;
    }

    public Weapon(String name, AmmoValue reloadCost, AmmoValue grabCost, String guiID) {
        this (name, reloadCost, grabCost);
        mGuiID = guiID;
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

    public String getGuiID() {
        return mGuiID;
    }

    // for GUI testing
    public void setGuiID(String guiID) {
        mGuiID = guiID;
    }

    public boolean isLoaded() {
        return mLoaded;
    }

    public Expression getBehaviour() {
        return mBehaviour;
    }

    // trivial setters
    public void setBehaviour(Behaviour behaviour) {
        mBehaviour = behaviour;
    }

    // TODO: add doc
    public void setLoaded(boolean value) {
        mLoaded = value;
    }

    // TODO: add doc
    // TODO: implement
    public Weapon deepCopy() {
        throw new UnsupportedOperationException();
    }

    public static List<Weapon> returnDeckFromJson(String json) {
        Gson gson = new Gson();
        Weapon[] weapons = gson.fromJson(json, Weapon[].class);

        return new ArrayList<>(Arrays.asList(weapons));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Weapon casted = (Weapon) o;

        return WeaponFactory.toJsonTree(this).equals(WeaponFactory.toJsonTree(casted));
    }

    @Override
    public int hashCode() {
        return Objects.hash(WeaponFactory.toJson(this));
    }

    // TODO: add doc
    @Override
    public String toString() {
        return WeaponFactory.toJson(this);
    }
}