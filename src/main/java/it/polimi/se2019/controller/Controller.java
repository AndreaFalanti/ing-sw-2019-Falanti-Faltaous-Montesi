package it.polimi.se2019.controller;


import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;


public class Controller implements Observable, Observer {

    private Game mGame;

    public Controller() {
    }

    public void performPlayerAction(Action action) {

    }

    public void requestPlayerAction() {

    }

    public Position getValidPosition(PlayerColor color){
        return null;
    }

    public void finalizeGrabAction(Position pos){

    }

    public void finalizeReload(Weapon weapon, Player player){

    }
    
}