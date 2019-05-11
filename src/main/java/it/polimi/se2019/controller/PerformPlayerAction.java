package it.polimi.se2019.controller;

import it.polimi.se2019.controller.responses.MessageActionResponse;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.util.Observable;

public class PerformPlayerAction {
    private Game mGame;
    private Action action;
    private MessageActionResponse message;

    public void update(Observable remoteView, Action action){
        this.action = action;
    }

    public void Execute(){
        if(action.isValid(mGame)){
            try{
                action.perform(mGame);

            }catch(Exception ex){
                action.getCode();
            }
        }
        else{
             action.getCode();
        }
    }
}

