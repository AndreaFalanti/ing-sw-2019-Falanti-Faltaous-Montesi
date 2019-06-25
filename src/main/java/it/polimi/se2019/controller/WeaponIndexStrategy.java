package it.polimi.se2019.controller;

import it.polimi.se2019.model.action.GrabAction;
import it.polimi.se2019.model.action.GrabWeaponAction;


public interface WeaponIndexStrategy {
    GrabAction completeAction (int index, GrabAction grabAction);

    static WeaponIndexStrategy exchangeWeapon () {
        return (index, action) -> {
            ((GrabWeaponAction)action).setWeaponToExchangeIndex(index);
            return action;
        };
    }

    static WeaponIndexStrategy grabWeapon () {
        return (index, action) -> new GrabWeaponAction(index);
    }
}
