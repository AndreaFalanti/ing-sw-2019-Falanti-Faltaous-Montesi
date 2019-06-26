package it.polimi.se2019.controller;

import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.model.action.CostlyAction;
import it.polimi.se2019.model.action.GrabAction;
import it.polimi.se2019.model.action.response.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;
import it.polimi.se2019.model.action.response.SelectWeaponRequiredActionResponse;
import it.polimi.se2019.view.View;

import java.util.Optional;

public class PlayerActionController implements InvalidActionResponseHandler {
    private Controller mMainController;
    private View mRequestingView;

    private Action mCachedAction;
    private GrabAction mCompletableGrabAction;
    private CostlyAction mCompletableCostlyAction;


    public PlayerActionController(Controller mainController) {
        mMainController = mainController;
    }

    public Action getCachedAction() {
        return mCachedAction;
    }

    public GrabAction getCompletableGrabAction() {
        return mCompletableGrabAction;
    }

    public CostlyAction getCompletableCostlyAction() {
        return mCompletableCostlyAction;
    }

    public void setCachedAction(Action cachedAction) {
        mCachedAction = cachedAction;
    }

    public void setCompletableGrabAction(GrabAction completableGrabAction) {
        mCompletableGrabAction = completableGrabAction;
    }

    private void resetCache () {
        mCachedAction = null;
        mCompletableCostlyAction = null;
        mCompletableGrabAction = null;
    }

    public void executeAction (Action action, View requestingView){
        mRequestingView = requestingView;
        resetCache();

        Optional<InvalidActionResponse> response = action.getErrorResponse(mMainController.getGame());
        if(!response.isPresent()) {
            action.perform(mMainController.getGame());
            if(action.consumeAction())
                mMainController.getGame().decreaseActionCounter();
        }
        else {
            mCachedAction = action;
            response.get().handle(this);
        }
    }

    @Override
    public void handle(MessageActionResponse actionResponse) {
        mRequestingView.showMessage(actionResponse.getMessage());
    }

    @Override
    public void handle(DiscardRequiredActionResponse actionResponse) {
        mCompletableCostlyAction = actionResponse.getCostlyAction();

        mRequestingView.showMessage(actionResponse.getMessage());
        mRequestingView.showPowerUpsDiscardView();
    }

    @Override
    public void handle(SelectWeaponRequiredActionResponse actionResponse) {
        mCompletableGrabAction = actionResponse.getGrabAction();
        mMainController.setWeaponIndexStrategy(actionResponse.getStrategy());

        mRequestingView.showMessage(actionResponse.getMessage());
        mRequestingView.showWeaponSelectionView(actionResponse.getColor());
    }
}

