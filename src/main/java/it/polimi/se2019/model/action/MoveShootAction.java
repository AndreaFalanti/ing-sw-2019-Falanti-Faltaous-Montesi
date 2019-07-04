package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.expression.Expression;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class MoveShootAction implements ShootLeadingAction {
    private MoveAction mMoveAction;
    private ShootAction mShootAction;

    public MoveShootAction (PlayerColor playerColor, Position destination, int weaponIndex) {
        mMoveAction = new MoveAction(playerColor, destination);
        mShootAction = new ShootAction(weaponIndex);
    }

    public MoveAction getMoveAction() {
        return mMoveAction;
    }

    public ShootAction getShootAction() {
        return mShootAction;
    }

    @Override
    public void perform(Game game) {
        mMoveAction.perform(game);
        mShootAction.perform(game);
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        // can't perform "costly" actions if there are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }

        Player player = game.getPlayerFromColor(mMoveAction.getTarget());
        int maxShootMoves;

        if (!game.isFinalFrenzy()) {
            if (!player.canMoveBeforeShooting()) {
                maxShootMoves = 0;
            }
            else {
                maxShootMoves = 1;
            }
        }
        else if (game.hasFirstPlayerDoneFinalFrenzy()) {
            maxShootMoves = 2;
        }
        else {
            maxShootMoves = 1;
        }

        if (game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) > maxShootMoves) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.ILLEGAL_TILE_DISTANCE + " while shooting"));
        }

        Optional<InvalidActionResponse> response = mShootAction.getErrorResponse(game);
        if (response.isPresent()) {
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
        return game.getActivePlayer().getWeapon(mShootAction.getWeaponIndex()).getBehaviour();
    }
}
