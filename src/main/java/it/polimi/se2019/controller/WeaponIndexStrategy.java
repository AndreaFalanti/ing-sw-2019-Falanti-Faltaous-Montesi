package it.polimi.se2019.controller;

import it.polimi.se2019.model.action.Action;

public interface WeaponIndexStrategy {
    Action completeAction (int index, Action action);

    /*static WeaponIndexStrategy grabWeapon () {
        return (index, action) -> new GrabWeaponAction(index, action);
    }*/
}
