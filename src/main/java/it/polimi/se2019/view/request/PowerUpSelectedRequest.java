package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

public class PowerUpSelectedRequest implements Request {
    private int mIndex;
    private View mView;

    public PowerUpSelectedRequest(int index, View view) {
        mIndex = index;
        mView = view;
    }

    public int getIndex() {
        return mIndex;
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
