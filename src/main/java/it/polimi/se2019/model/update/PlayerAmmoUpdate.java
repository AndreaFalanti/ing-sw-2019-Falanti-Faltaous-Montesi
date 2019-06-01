package it.polimi.se2019.model.update;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;

public class PlayerAmmoUpdate implements Update {
    private PlayerColor mPlayerColor;
    private AmmoValue mPlayerAmmo;

    public PlayerAmmoUpdate(PlayerColor playerColor, AmmoValue playerAmmo) {
        mPlayerColor = playerColor;
        mPlayerAmmo = playerAmmo;
    }

    public PlayerColor getPlayerColor() {
        return mPlayerColor;
    }

    public AmmoValue getPlayerAmmo() {
        return mPlayerAmmo;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
