package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;
import it.polimi.se2019.controller.WeaponIndexStrategy;
import it.polimi.se2019.model.action.GrabAction;
import it.polimi.se2019.model.board.TileColor;

/**
 * Action error message thrown when player is required to select a weapon
 *
 * @author Andrea Falanti
 */
public class SelectWeaponRequiredActionResponse extends MessageActionResponse {
    // null if player's hand
    private TileColor mColor;
    private WeaponIndexStrategy mStrategy;
    private GrabAction mGrabAction;

    public SelectWeaponRequiredActionResponse(String message, TileColor color,
                                              WeaponIndexStrategy strategy, GrabAction grabAction) {
        super(message, false);
        mColor = color;
        mStrategy = strategy;
        mGrabAction = grabAction;
    }

    public TileColor getColor() {
        return mColor;
    }

    public WeaponIndexStrategy getStrategy() {
        return mStrategy;
    }

    public GrabAction getGrabAction() {
        return mGrabAction;
    }

    @Override
    public void handle(InvalidActionResponseHandler handler) {
        handler.handle(this);
    }
}
