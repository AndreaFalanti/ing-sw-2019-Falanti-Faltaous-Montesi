package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.DiscardRequiredActionResponse;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

/**
 * Action for performing a weapon reload
 *
 * @author Andrea Falanti
 */
public class ReloadAction implements CostlyAction {
    private int mWeaponIndex;
    private boolean[] mDiscardPowerUp = {false, false, false};

    public ReloadAction (int weaponIndex) {
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

    @Override
    public boolean[] getDiscardedCards() {
        return mDiscardPowerUp;
    }

    @Override
    public void setDiscardedCards(boolean[] discardedCards) {
        mDiscardPowerUp = discardedCards;
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

        if (mWeaponIndex < 0 || mWeaponIndex >= 3) {
            return Optional.of(new MessageActionResponse("Invalid weapon index selected"));
        }

        Weapon weaponToReload = player.getWeapon(mWeaponIndex);
        // can't reload an already loaded weapon or a null weapon
        if (weaponToReload == null) {
            return Optional.of(new MessageActionResponse("Invalid weapon index selected"));
        }
        if (weaponToReload.isLoaded()) {
            return Optional.of(new MessageActionResponse("Weapon selected is already loaded"));
        }

        // reload action can be performed only on turn end if not composed in a final frenzy action
        if (!game.isFinalFrenzy() && game.getRemainingActions() != 0) {
            return Optional.of(new MessageActionResponse("Can't reload weapon if there are still actions available"));
        }

        if (!AmmoPayment.isValid(player, weaponToReload.getReloadCost(), mDiscardPowerUp)) {
            AmmoValue remainingCost = weaponToReload.getGrabCost().subtract(player.getAmmo(), true);
            return AmmoPayment.canPayWithPowerUps(player, remainingCost) ?
                    Optional.of(new DiscardRequiredActionResponse(ActionResponseStrings.DISCARD_MESSAGE, this)) :
                    Optional.of(new MessageActionResponse(ActionResponseStrings.NOT_ENOUGH_AMMO));
        }

        return Optional.empty();
    }

    @Override
    public boolean consumeAction() {
        return false;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public boolean leadToAShootInteraction() {
        return false;
    }
}