package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class MoveAction implements Action {
    private PlayerColor mTarget;
    private Position mDestination;
    private boolean mNormalAction;

    /**
     * Simplified constructor for weapon additional effects
     * @param playerColor Player color of the target
     * @param destination Target destination
     */
    public MoveAction(PlayerColor playerColor, Position destination) {
        mTarget = playerColor;
        mDestination = destination;
        mNormalAction = false;
    }

    public MoveAction(PlayerColor playerColor, Position destination, boolean isNormalMove) {
        if (destination == null) {
            throw new IllegalArgumentException("Can't move to null position!");
        }
        mTarget = playerColor;
        mDestination = destination;
        mNormalAction = isNormalMove;
    }

    public PlayerColor getTarget() {
        return mTarget;
    }

    public Position getDestination() {
        return mDestination;
    }

    public boolean isNormalAction() {
        return mNormalAction;
    }

    @Override
    public void perform(Game game) {
        game.getPlayerFromColor(mTarget).move(mDestination);
    }

    @Override
    public boolean isValid(Game game) {
        Position playerPos = game.getPlayerFromColor(mTarget).getPos();

        if (mNormalAction) {
            // player can't move himself if out of actions
            if (game.getRemainingActions() == 0) {
                return false;
            }

            if (!game.isFinalFrenzy()) {
                return game.getBoard().getTileDistance(playerPos, mDestination) <= 3;
            }
            else if (!game.hasFirstPlayerDoneFinalFrenzy()) {
                return game.getBoard().getTileDistance(playerPos, mDestination) <= 4;
            }
            // you can't only move if is final frenzy and you are after first player.
            else {
                return false;
            }
        }
        else {
            // maximum distance for "indirect" moves are 3 spaces
            return game.getBoard().getTileDistance(playerPos, mDestination) <= 3;
        }
    }
}
