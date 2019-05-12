package it.polimi.se2019.controller;


import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.View;


public class Controller implements Observable, Observer {

    private Game mGame;
    private PerformPlayerAction mPerform;//change name
  //  private requestPlayerAction mRequest;//change name
  //  private getValidPosition mValidPostion;//change name
    private TakeLeaderboard mLeaderboard;
    private View mRemoteView;

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