package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

public class WeaponModeSelectedRequest implements Request {
    private String mId;
    private View mView;

    public WeaponModeSelectedRequest(String id, View view) {
        mId = id;
        mView = view;
    }

    public String getId() {
        return mId;
    }

    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
