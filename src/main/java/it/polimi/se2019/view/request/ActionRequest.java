package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.action.Action;

public class ActionRequest implements Request {
    private Action mAction;

    public ActionRequest(Action action) {
        mAction = action;
    }

    private Action getAction() {
        return mAction;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
