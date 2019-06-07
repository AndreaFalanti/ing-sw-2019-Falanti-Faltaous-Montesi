package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;

public class PowerUpDiscardedRequest implements Request {
    private boolean[] mDiscarded;

    public PowerUpDiscardedRequest(boolean[] discarded) {
        mDiscarded = discarded;
    }

    public boolean[] getDiscarded() {
        return mDiscarded;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
