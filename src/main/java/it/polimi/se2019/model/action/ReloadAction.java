package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.action.responses.ActionResponseStrings;
import it.polimi.se2019.model.action.responses.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;
import it.polimi.se2019.model.action.responses.MessageActionResponse;
import it.polimi.se2019.model.weapon.Weapon;

import java.util.Optional;

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
        Player player = game.getActivePlayer();

        // discard powerUps set in boolean array and add ammo to player
        AmmoPayment.addAmmoAndDiscard(player, mDiscardPowerUp);

        player.reloadWeapon(mWeaponIndex);
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        Player player = game.getActivePlayer();

        Weapon weaponToReload = player.getWeapon(mWeaponIndex);

        // can't reload an already loaded weapon or a null weapon
        if (weaponToReload == null || weaponToReload.isLoaded()) {
            return Optional.of(new MessageActionResponse("Weapon is null or already loaded"));
        }

        // reload action can be performed only on turn end if not composed in a final frenzy action
        if (!game.isFinalFrenzy() && game.getRemainingActions() != 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.HACKED_MOVE));
        }

        return AmmoPayment.isValid(player, weaponToReload.getReloadCost(), mDiscardPowerUp) ?
                Optional.empty() : Optional.of(new DiscardRequiredActionResponse(ActionResponseStrings.DISCARD_MESSAGE));
    }

    @Override
    public boolean consumeAction() {
        return false;
    }
}