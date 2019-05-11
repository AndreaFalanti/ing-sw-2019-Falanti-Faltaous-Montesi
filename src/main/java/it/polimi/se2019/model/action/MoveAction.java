package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class MoveAction implements Action {
    private PlayerColor mTarget;
    private Position mDestination;
    private boolean mNormalMove;
    private ResponseCode mCode;
    private String message;

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
    public boolean isValid(Game game) {
        Position playerPos = game.getPlayerFromColor(mTarget).getPos();

        // can't move player to its precedent position
        if (playerPos.equals(mDestination)) {
            return false;
        }

        if (mNormalMove) {
            // player can't move himself if out of actions
            if (game.getRemainingActions() == 0) {
                System.out.println("Max number of action reached");
                this.mCode = ResponseCode.NO_ACTION_LEFT;
                return false;
            }

            // normal action is only set for active players to move themselves
            if (game.getActivePlayer().getColor() != mTarget) {
                System.out.println("You can only move yourself");
                this.mCode = ResponseCode.PERFORMABLE_BY_ACTIVE_PLAYER;
                return false;
            }

            // max moves in "normal" state is 3
            if (!game.isFinalFrenzy()) {
                if(game.getBoard().getTileDistance(playerPos, mDestination) <= 3) {
                    this.mCode = ResponseCode.OK;
                    return true;
                }
                else {
                    this.mCode = ResponseCode.TILE_NOT_REACHABLE;
                    return false;
                }
            }
            // you can move 4 tiles if you are before first player in final frenzy status.
            else if (!game.hasFirstPlayerDoneFinalFrenzy()) {
                    if(game.getBoard().getTileDistance(playerPos, mDestination) <= 4){
                        this.mCode = ResponseCode.OK;
                        return true;
                    }
                    else {
                        this.mCode = ResponseCode.TILE_NOT_REACHABLE;
                        return false;
                    }
            }
            // you can't only move if is final frenzy and you are after first player.
            else {
                this.mCode = ResponseCode.CANT_MOVE;
                return false;
            }
        }
        else {
            // maximum distance for "indirect" moves are 3 spaces
            //TODO --> this.mCode
            return game.getBoard().getTileDistance(playerPos, mDestination) <= 3;
        }
    }

    @Override
    public boolean consumeAction() {
        return mNormalMove;
    }

    public ResponseCode getCode(){return mCode;}
}
