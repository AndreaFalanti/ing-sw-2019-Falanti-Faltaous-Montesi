package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class MoveShootAction implements Action {
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
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }

        Player player = game.getPlayerFromColor(mMoveAction.getTarget());
        int maxShootMoves;

        if (!game.isFinalFrenzy()) {
            if (!player.canMoveBeforeShooting()) {
                return Optional.of(new MessageActionResponse("You can't move while shooting right now"));
            }
            maxShootMoves = 1;
        }
        else if (game.hasFirstPlayerDoneFinalFrenzy()) {
            maxShootMoves = 2;
        }
        else {
            maxShootMoves = 1;
        }
            return game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) == maxShootMoves ?
                    Optional.empty() : Optional.of(
                            new MessageActionResponse(ActionResponseStrings.ILLEGAL_TILE_DISTANCE + " while shooting")
            );
    }

    @Override
    public boolean consumeAction() {
        return true;
    }

    @Override
    public boolean isComposite() {
        return true;
    }
}
