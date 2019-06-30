package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;
import it.polimi.se2019.model.action.CostlyAction;

public class DiscardRequiredActionResponse extends MessageActionResponse {
    private CostlyAction mCostlyAction;

    public DiscardRequiredActionResponse(String message, CostlyAction costlyAction) {
        super(message);
        mCostlyAction = costlyAction;
    }

    public CostlyAction getCostlyAction() {
        return mCostlyAction;
    }

    @Override
    public void handle(InvalidActionResponseHandler handler) {
        handler.handle(this);
    }
}
