package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class MoveAction implements Action {
    private PlayerColor mTarget;
    private Position mDestination;
    private boolean mNormalMove;

    /**
     * Simplified constructor for weapon additional effects
     * @param playerColor Player color of the target
     * @param destination Target destination
     */
    public MoveAction(PlayerColor playerColor, Position destination) {
        if (destination == null) {
            throw new IllegalArgumentException("Can't move to null position!");
        }
        mTarget = playerColor;
        mDestination = destination;
        mNormalMove = false;
    }

    /**
     * Complete constructor
     * @param playerColor Player color of the target
     * @param destination Target destination
     * @param isNormalMove Specify if is active player moving as action (use false for card effects)
     */
    public MoveAction(PlayerColor playerColor, Position destination, boolean isNormalMove) {
        this(playerColor, destination);
        mNormalMove = isNormalMove;
    }

    public PlayerColor getTarget() {
        return mTarget;
    }

    public Position getDestination() {
        return mDestination;
    }

    public boolean isNormalMove() {
        return mNormalMove;
    }

    @Override
    public void perform(Game game) {
        game.getPlayerFromColor(mTarget).move(mDestination);
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        Position playerPos = game.getPlayerFromColor(mTarget).getPos();

        // can't move player to its precedent position
        if (playerPos.equals(mDestination)) {
            return Optional.of(new MessageActionResponse("Can't move in your current position!"));
        }

        if (mNormalMove) {
            // player can't move himself if out of actions
            if (game.getRemainingActions() == 0) {
                return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
            }

            // normal action is only set for active players to move themselves
            if (game.getActivePlayer().getColor() != mTarget) {
                return Optional.of(new MessageActionResponse(ActionResponseStrings.HACKED_MOVE));
            }

            int moveMaxDistance;

            // set max moves based on actual game state
            // max moves in "normal" state is 3
            if (!game.isFinalFrenzy()) {
                moveMaxDistance = 3;
            }
            // you can move 4 tiles if you are before first player in final frenzy status.
            else if (!game.hasFirstPlayerDoneFinalFrenzy()) {
                moveMaxDistance = 4;
            }
            // you can't only move if is final frenzy and you are after first player.
            else {
                return Optional.of(
                        new MessageActionResponse("You are after the first player and is final frenzy, so you can't move")
                );
            }

            // check that move distance is equals or less of set parameter
            return game.getBoard().getTileDistance(playerPos, mDestination) <= moveMaxDistance ?
                    Optional.empty() : Optional.of(new MessageActionResponse(ActionResponseStrings.ILLEGAL_TILE_DISTANCE));
        }
        else {
            // maximum distance for "indirect" moves are 3 spaces
            return game.getBoard().getTileDistance(playerPos, mDestination) <= 3 ?
                    Optional.empty() : Optional.of(new MessageActionResponse("You can shift a player up to three tiles"));
        }
    }

    @Override
    public boolean consumeAction() {
        return mNormalMove;
    }
}
