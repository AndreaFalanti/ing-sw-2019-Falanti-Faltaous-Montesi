package it.polimi.se2019.controller;

import it.polimi.se2019.controller.response.MessageActionResponse;
import it.polimi.se2019.controller.response.Response;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.response.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.SelectWeaponRequiredActionResponse;
import it.polimi.se2019.util.Observable;

import java.util.Optional;

public class PerformPlayerAction implements InvalidActionResponseHandler {
    private Game mGame;
    private Action mAction;
    private MessageActionResponse message;

    public void update(Observable remoteView, Action action){
        mAction = action;
    }

    public void Execute(){
        Optional<InvalidActionResponse> response = mAction.getErrorResponse(mGame);
        if(!response.isPresent()) {
            mAction.perform(mGame);
            if(mAction.consumeAction())
                mGame.decreaseActionCounter();
        }
        else {
             response.get().handle(this);
        }
    }

    @Override
    public Response handle(it.polimi.se2019.model.action.response.MessageActionResponse actionResponse) {
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

