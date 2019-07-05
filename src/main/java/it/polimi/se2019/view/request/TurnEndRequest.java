package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

/**
 * Request to controller to end turn
 *
 * @author Andrea Falanti
 */
public class TurnEndRequest implements Request {
    private PlayerColor mViewColor;

    public TurnEndRequest(PlayerColor viewColor) {
        mViewColor = viewColor;
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
