package it.polimi.se2019.model.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.ShootResult;
import it.polimi.se2019.model.weapon.serialization.WeaponFactory;
import it.polimi.se2019.util.Exclude;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Weapon {
    private String mName;
    private AmmoValue mReloadCost;
    private AmmoValue mGrabCost;
    private String mGuiID;

    @Exclude
    private boolean mLoaded;

    // weapon behaviour
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
    public void setBehaviour(Expression behaviour) {
        mBehaviour = behaviour;
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
    // TODO: implement this better
    public ShootResult shoot(ShootContext shootContext) {
        return getBehaviour().evalToShootResult(shootContext);
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