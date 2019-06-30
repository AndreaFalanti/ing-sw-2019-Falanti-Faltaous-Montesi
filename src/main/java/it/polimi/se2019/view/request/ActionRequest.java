package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.action.Action;

public class ActionRequest implements Request {
    private Action mAction;
    private PlayerColor mViewColor;

    public ActionRequest(Action action, PlayerColor viewColor) {
        mAction = action;
        mViewColor = viewColor;
    }

    public Action getAction() {
        return mAction;
    }

    @Override
    public PlayerColor getViewColor() {
        return mViewColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
