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
        // player can't move himself if out of actions
        if (mNormalAction && game.getRemainingActions() == 0) {
            return false;
        }
        else {
            Position playerPos = game.getPlayerFromColor(mTarget).getPos();
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
    }
}
