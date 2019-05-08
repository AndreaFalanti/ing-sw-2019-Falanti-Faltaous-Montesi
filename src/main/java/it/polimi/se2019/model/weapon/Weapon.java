package it.polimi.se2019.model.weapon;

import com.google.gson.Gson;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Deck;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.request.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Weapon {
    private String mName;
    private AmmoValue mReloadCost;
    private AmmoValue mGrabCost;

    // behaviour of weapon used to shoot
    Expression mBehaviour;

    // used in tests
    public Weapon () {}

    // trivial constructor
    public Weapon(Expression behaviour) {
        mBehaviour = behaviour;
    }

    // trivial getters
    // TODO: implement loading
    public boolean isLoaded() {
        return true;
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

    public String getName() {
        return mName;
    }

    public AmmoValue getReloadCost() {
        return mReloadCost;
    }

    public AmmoValue getGrabCost() {
        return mGrabCost;
    }

    // TODO: add doc
    // TODO: implement
    public void setLoaded(boolean loaded) {
        ;
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