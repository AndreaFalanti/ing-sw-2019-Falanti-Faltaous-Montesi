package it.polimi.se2019.model.weapon;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.behaviour.ShootContext;
import it.polimi.se2019.model.weapon.behaviour.Expression;
import it.polimi.se2019.model.weapon.request.Request;


public class Weapon {
    // behaviour of weapon used to shoot
    Expression mBehaviour;

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

    // TODO: add doc
    // TODO: implement
    public AmmoValue getReloadCost() {
        return null;
    }

    // TODO: add doc
    // TODO: implement
    public void setLoaded(boolean loaded) {
        ;
    }
}