package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

/**
 * Request a shoot interaction undo to controller
 *
 * @author Andrea Falanti
 */
public class UndoWeaponInteractionRequest implements Request {
    private PlayerColor mViewColor;

    public UndoWeaponInteractionRequest(PlayerColor viewColor) {
        mViewColor = viewColor;
    }

    public PlayerColor getViewColor() {
        return mViewColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
