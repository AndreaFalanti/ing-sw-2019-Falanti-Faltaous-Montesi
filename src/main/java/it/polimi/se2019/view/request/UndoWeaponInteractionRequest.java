package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

public class UndoWeaponInteractionRequest implements Request {
    private View mView;

    public UndoWeaponInteractionRequest(View view) {
        mView = view;
    }

    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
