package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

public class PlayerRespawnUpdate implements Update {
    private PlayerColor mPlayerColor;

    public PlayerRespawnUpdate(PlayerColor playerColor) {
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
