package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.weapon.Weapon;

public class ReloadAction implements Action {
    private int mWeaponIndex;
    private boolean[] mDiscardPowerUp = {false, false, false};

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
        game.getActivePlayer().reloadWeapon(mWeaponIndex);
        for (int i = 0; i < mDiscardPowerUp.length && mDiscardPowerUp[i]; i++) {
            game.getActivePlayer().discard(i);
        }
    }

    @Override
    public boolean isValid(Game game) {
        AmmoValue playerAmmo = game.getActivePlayer().getAmmo();
        Weapon weaponToReload = game.getActivePlayer().getWeapon(mWeaponIndex);
        boolean isDiscarding = false;

        // reload action can be performed only on turn end if not composed in a final frenzy action
        if (!game.isFinalFrenzy() && game.getRemainingActions() != 0) {
            return false;
        }

        // check that user is trying to discard a valid card
        for (int i = 0; i < mDiscardPowerUp.length && mDiscardPowerUp[i]; i++) {
            isDiscarding = true;
            if (game.getActivePlayer().getPowerUpCard(i) == null) {
                return false;
            }
        }

        if (playerAmmo.isBiggerOrEqual(weaponToReload.getReloadCost())) {
            return !isDiscarding && !weaponToReload.isLoaded();
        }
        else {
            AmmoValue ammoWithPowerUps = getAmmoTotalWithPowerUpDiscard(game.getActivePlayer());
            return ammoWithPowerUps.isBiggerOrEqual(game.getActivePlayer().getWeapon(mWeaponIndex).getReloadCost());
        }
    }

    private AmmoValue getAmmoTotalWithPowerUpDiscard (Player player) {
        AmmoValue ammo = player.getAmmo().deepCopy();
        for (int i = 0; i < mDiscardPowerUp.length  && mDiscardPowerUp[i]; i++) {
            ammo.add(player.getPowerUpCard(i).getAmmoValue());
        }

        return ammo;
    }
}