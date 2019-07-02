package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class ShootAction implements ShootLeadingAction {
    private int mWeaponIndex;

    public ShootAction(int weaponIndex) {
        mWeaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    @Override
    public void perform(Game game) {
        game.getActivePlayer().getWeapon(mWeaponIndex).setLoaded(false);
    }

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

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public boolean leadToAShootInteraction() {
        return true;
    }

    @Override
    public Expression getShotBehaviour(Game game) {
        return game.getActivePlayer().getWeapon(mWeaponIndex).getBehaviour();
    }
}