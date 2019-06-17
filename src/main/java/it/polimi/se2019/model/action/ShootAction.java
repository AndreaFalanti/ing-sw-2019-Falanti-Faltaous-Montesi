package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.Tile;
import it.polimi.se2019.view.View;

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
    public void perform(Game game) {
        throw new UnsupportedOperationException("WIP");
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
}