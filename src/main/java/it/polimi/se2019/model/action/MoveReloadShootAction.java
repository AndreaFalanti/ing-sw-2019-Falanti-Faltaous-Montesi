package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.responses.ActionResponseStrings;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;
import it.polimi.se2019.model.action.responses.MessageActionResponse;

import java.util.Optional;

public class MoveReloadShootAction implements Action {
    private MoveShootAction mMoveShootAction;
    private ReloadAction mReloadAction;

    public MoveReloadShootAction (PlayerColor playerColor, Position destination, int weaponIndex) {
        mMoveShootAction = new MoveShootAction(playerColor, destination);
        mReloadAction = new ReloadAction(weaponIndex);
    }

    public MoveShootAction getMoveShootAction() {
        return mMoveShootAction;
    }

    public ReloadAction getReloadAction() {
        return mReloadAction;
    }

    @Override
    public void perform(Game game) {
        mReloadAction.perform(game);
        mMoveShootAction.perform(game);
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }
        if (!game.isFinalFrenzy()) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.HACKED_MOVE));
        }

        Optional<InvalidActionResponse> response = mMoveShootAction.getErrorResponse(game);
        if (response.isPresent()) {
            return response;
        }

        return mReloadAction.getErrorResponse(game);
    }

    @Override
    public boolean consumeAction() {
        return true;
    }
}
