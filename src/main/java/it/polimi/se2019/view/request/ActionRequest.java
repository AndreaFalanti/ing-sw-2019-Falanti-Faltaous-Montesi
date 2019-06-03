package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.action.Action;

public class ActionRequest implements Request {
    Action mAction;

    Action getAction() {
        return mAction;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
