package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class ShootAction implements Action {
    private int mWeaponIndex;

    public ShootAction(int weaponIndex) {
        mWeaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    @Override
    public void perform(Game game) {}

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }

        return Optional.empty();
    }

    @Override
    public boolean consumeAction() {
        return true;
    }
}