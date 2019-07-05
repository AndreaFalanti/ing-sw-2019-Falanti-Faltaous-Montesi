package it.polimi.se2019.model.update;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.PlayerColor;

/**
 * Update message with player's weapon info, sent to views
 *
 * @author Andrea Falanti
 */
public class PlayerWeaponsUpdate implements Update {
    private PlayerColor mPlayerColor;
    private Weapon[] mWeapons;

    public PlayerWeaponsUpdate(PlayerColor playerColor, Weapon[] weapons) {
        mPlayerColor = playerColor;
        mWeapons = weapons;
    }

    public PlayerColor getPlayerColor() {
        return mPlayerColor;
    }

    public Weapon[] getWeapons() {
        return mWeapons;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
