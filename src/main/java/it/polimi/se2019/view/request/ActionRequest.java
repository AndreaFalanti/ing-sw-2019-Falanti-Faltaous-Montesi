package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.action.Action;
import it.polimi.se2019.view.View;

public class ActionRequest implements Request {
    private Action mAction;
    private View mView;

    public ActionRequest(Action action, View view) {
        mAction = action;
        mView = view;
    }

    public Action getAction() {
        return mAction;
    }

    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
