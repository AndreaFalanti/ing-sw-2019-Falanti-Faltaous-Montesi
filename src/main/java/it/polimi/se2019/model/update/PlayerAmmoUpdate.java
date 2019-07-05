package it.polimi.se2019.model.update;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.PlayerColor;

/**
 * Update message with player's ammo info, sent to views
 *
 * @author Andrea Falanti
 */
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
