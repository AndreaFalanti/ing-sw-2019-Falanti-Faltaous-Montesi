package it.polimi.se2019.controller;

import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.response.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;
import it.polimi.se2019.model.action.response.SelectWeaponRequiredActionResponse;
import it.polimi.se2019.view.View;

import java.util.Optional;

public class PlayerActionController implements InvalidActionResponseHandler {
    private Controller mMainController;
    private View mRequestingView;

    public PlayerActionController(Controller mainController) {
        mMainController = mainController;
    }

    public void executeAction (Action action, View requestingView){
        mRequestingView = requestingView;

        Optional<InvalidActionResponse> response = action.getErrorResponse(mMainController.getGame());
        if(!response.isPresent()) {
            action.perform(mMainController.getGame());
            if(action.consumeAction())
                mMainController.getGame().decreaseActionCounter();
        }
        else {
             response.get().handle(this);
        }
    }

    @Override
    public void handle(MessageActionResponse actionResponse) {
        mRequestingView.showMessage(actionResponse.getMessage());
    }

    @Override
    public void handle(DiscardRequiredActionResponse actionResponse) {

    }

    @Override
    public void handle(SelectWeaponRequiredActionResponse actionResponse) {

    }
}

