package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class MoveReloadShootAction implements ShootLeadingAction {
    private MoveShootAction mMoveShootAction;
    private ReloadAction mReloadAction;

    public MoveReloadShootAction (PlayerColor playerColor, Position destination,
                                  int shootWeaponIndex, int reloadWeaponIndex) {
        mMoveShootAction = new MoveShootAction(playerColor, destination, shootWeaponIndex);
        mReloadAction = new ReloadAction(reloadWeaponIndex);
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
        // can't perform "costly" actions if there are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }
        if (!game.isFinalFrenzy()) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.HACKED_MOVE));
        }

        Optional<InvalidActionResponse> response = mReloadAction.getErrorResponse(game);
        if (response.isPresent()) {
            return response;
        }

        response = mMoveShootAction.getErrorResponse(game);
        if (response.isPresent() &&
                !((MessageActionResponse)response.get()).getMessage().equals("Trying to shoot with an unloaded weapon!")) {
                return response;
        }

        return Optional.empty();
    }

    @Override
    public boolean consumeAction() {
        return true;
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public boolean leadToAShootInteraction() {
        return true;
    }

    @Override
    public Expression getShotBehaviour(Game game) {
        return game.getActivePlayer().getWeapon(mMoveShootAction.getShootAction().getWeaponIndex()).getBehaviour();
    }
}
