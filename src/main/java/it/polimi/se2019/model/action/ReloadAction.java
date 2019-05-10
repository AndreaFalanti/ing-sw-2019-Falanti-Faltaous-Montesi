package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.weapon.Weapon;

public class ReloadAction implements Action {
    private int mWeaponIndex;
    private boolean[] mDiscardPowerUp = {false, false, false};
    private ResponseCode mCode;
    private String message;

    public ReloadAction (int weaponIndex) {
        if (weaponIndex < 0 || weaponIndex >= 3) {
            throw new IllegalArgumentException("Illegal weapon index in Reload action");
        }
        mWeaponIndex = weaponIndex;
    }

    public ReloadAction (int weaponIndex, boolean[] discardPowerUp) {
        this(weaponIndex);
        if (discardPowerUp.length != 3) {
            throw new IllegalArgumentException("Illegal boolean array");
        }
        mDiscardPowerUp = discardPowerUp;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    public boolean[] getDiscardPowerUp() {
        return mDiscardPowerUp;
    }

    @Override
    public void perform(Game game) {
        Player player = game.getActivePlayer();

        // discard powerUps set in boolean array and add ammo to player
        for (int i = 0; i < mDiscardPowerUp.length; i++) {
            if (mDiscardPowerUp[i]) {
                player.getAmmo().add(player.getPowerUpCard(i).getAmmoValue());
                player.discard(i);
            }
        }

        player.reloadWeapon(mWeaponIndex);
    }

    @Override
    public boolean isValid(Game game) {
        AmmoValue playerAmmo = game.getActivePlayer().getAmmo();
        Weapon weaponToReload = game.getActivePlayer().getWeapon(mWeaponIndex);
        boolean isDiscarding = false;

        // can't reload an already loaded weapon or a null weapon
        if (weaponToReload == null || weaponToReload.isLoaded()) {
            return false;
        }

        // reload action can be performed only on turn end if not composed in a final frenzy action
        if (!game.isFinalFrenzy() && game.getRemainingActions() != 0) {
            return false;
        }

        // check that user is trying to discard a valid card (not null)
        for (int i = 0; i < mDiscardPowerUp.length; i++) {
            if (mDiscardPowerUp[i]) {
                isDiscarding = true;
                if (game.getActivePlayer().getPowerUpCard(i) == null) {
                    return false;
                }
            }
        }

        // check that if player can pay the cost directly, it's not discarding power up cards
        if (playerAmmo.isBiggerOrEqual(weaponToReload.getReloadCost())) {
            return !isDiscarding;
        }
        else {
            AmmoValue ammoWithPowerUps = getAmmoTotalWithPowerUpDiscard(game.getActivePlayer());
            return ammoWithPowerUps.isBiggerOrEqual(weaponToReload.getReloadCost());
        }
    }

    /**
     * Get total ammo considering also power up card discarded
     * @param player Player that is performing the reload
     * @return Total ammo with power up bonus ones
     */
    private AmmoValue getAmmoTotalWithPowerUpDiscard (Player player) {
        AmmoValue ammo = player.getAmmo().deepCopy();
        for (int i = 0; i < mDiscardPowerUp.length; i++) {
            if (mDiscardPowerUp[i]) {
                ammo.add(player.getPowerUpCard(i).getAmmoValue());
            }
        }

        return ammo;
    }

    public ResponseCode getCode(){return mCode;}
}