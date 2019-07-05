package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

/**
 * Contains discarded powerUp index used to respawn, to send to controller
 *
 * @author Andrea Falanti
 */
public class RespawnPowerUpRequest implements Request {
    private int mIndex;
    private PlayerColor mViewColor;

    public RespawnPowerUpRequest(int index, PlayerColor viewColor) {
        mIndex = index;
        mViewColor = viewColor;
    }

    public int getIndex() {
        return mIndex;
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
