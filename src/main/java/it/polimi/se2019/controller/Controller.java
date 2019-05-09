package it.polimi.se2019.controller;


import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;


public class Controller implements Observable, Observer {

    private Game mGame;
    private PerformPlayerAction mPerform;//change name
  //  private requestPlayerAction mRequest;//change name
  //  private getLeaderBoard mLeaderBoard;//change name
  //  private getValidPosition mValidPostion;//change name

    public Controller() {
    }

    public void performPlayerAction(Action action) {

    }

    public void requestPlayerAction() {

    }

    public Position[] getValidPosition(PlayerColor color){
        return null;
    }//return all valid positions according to action


    public void getLeaderBoard(){

    }
}