package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

/**
 * Update message with flipped board flag info (related to final frenzy), sent to views
 *
 * @author Andrea Falanti
 */
public class PlayerBoardFlipUpdate implements Update {
    private PlayerColor mPlayerColor;

    public PlayerBoardFlipUpdate(PlayerColor playerColor) {
        mPlayerColor = playerColor;
    }

    public PlayerColor getPlayerColor() {
        return mPlayerColor;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
