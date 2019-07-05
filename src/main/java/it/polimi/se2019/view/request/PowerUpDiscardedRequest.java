package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

/**
 * Contains discarded powerUp values to send to controller
 *
 * @author Andrea Falanti
 */
public class PowerUpDiscardedRequest implements Request {
    private boolean[] mDiscarded;
    private PlayerColor mViewColor;

    public PowerUpDiscardedRequest(boolean[] discarded, PlayerColor viewColor) {
        mDiscarded = discarded;
        mViewColor = viewColor;
    }

    public boolean[] getDiscarded() {
        return mDiscarded;
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
