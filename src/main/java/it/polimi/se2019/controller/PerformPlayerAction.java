package it.polimi.se2019.controller;

import it.polimi.se2019.controller.responses.MessageActionResponse;
import it.polimi.se2019.controller.responses.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.responses.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;
import it.polimi.se2019.model.action.responses.SelectWeaponRequiredActionResponse;
import it.polimi.se2019.util.Observable;

public class PerformPlayerAction implements InvalidActionResponseHandler {
    private Game mGame;
    private Action mAction;
    private MessageActionResponse message;

    public void update(Observable remoteView, Action action){
        mAction = action;
    }

    public void Execute(){
        InvalidActionResponse response = mAction.getErrorResponse(mGame);
        if(response == null) {
            mAction.perform(mGame);
            if(mAction.consumeAction())
                mGame.decreaseActionCounter();
        }
        else {
             response.handle(this);
        }
    }

    @Override
    public Response handle(it.polimi.se2019.model.action.responses.MessageActionResponse actionResponse) {
        return null;
    }

    @Override
    public Response handle(DiscardRequiredActionResponse actionResponse) {
        return null;
    }

    @Override
    public Response handle(SelectWeaponRequiredActionResponse actionResponse) {
        return null;
    }
}

