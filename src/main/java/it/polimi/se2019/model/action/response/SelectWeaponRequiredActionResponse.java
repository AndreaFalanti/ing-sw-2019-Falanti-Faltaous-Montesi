package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;
import it.polimi.se2019.model.board.TileColor;

public class SelectWeaponRequiredActionResponse extends MessageActionResponse {
    // null if player's hand
    private TileColor mColor;

    public SelectWeaponRequiredActionResponse(String message, TileColor color) {
        super(message);
        mColor = color;
    }

    public TileColor getColor() {
        return mColor;
    }

    @Override
    public void handle(InvalidActionResponseHandler handler) {
        handler.handle(this);
    }
}
