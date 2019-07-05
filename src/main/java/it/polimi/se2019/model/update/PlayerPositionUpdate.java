package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

/**
 * Update message with player's position info, sent to views
 *
 * @author Andrea Falanti
 */
public class PlayerPositionUpdate implements Update {
    private PlayerColor mPlayerColor;
    private Position mPlayerPos;

    public PlayerPositionUpdate(PlayerColor playerColor, Position playerPos) {
        mPlayerColor = playerColor;
        mPlayerPos = playerPos;
    }

    public PlayerColor getPlayerColor() {
        return mPlayerColor;
    }

    public Position getPlayerPos() {
        return mPlayerPos;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
