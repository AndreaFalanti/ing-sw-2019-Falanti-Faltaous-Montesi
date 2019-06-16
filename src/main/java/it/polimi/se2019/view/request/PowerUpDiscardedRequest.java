package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

public class PowerUpDiscardedRequest implements Request {
    private boolean[] mDiscarded;
    private View mView;

    public PowerUpDiscardedRequest(boolean[] discarded, View view) {
        mDiscarded = discarded;
        mView = view;
    }

    public boolean[] getDiscarded() {
        return mDiscarded;
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
