package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.responses.ActionResponseStrings;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;
import it.polimi.se2019.model.action.responses.MessageActionResponse;

public class MoveGrabAction implements Action {
    private MoveAction mMoveAction;
    private GrabAction mGrabAction;

    public MoveGrabAction (PlayerColor playerColor, Position destination) {
        mMoveAction = new MoveAction(playerColor, destination);
        mGrabAction = new GrabAmmoAction();
    }

    public MoveGrabAction (PlayerColor playerColor, Position destination, int weaponIndex) {
        mMoveAction = new MoveAction(playerColor, destination);
        mGrabAction = new GrabWeaponAction(weaponIndex);
    }

    public MoveGrabAction (PlayerColor playerColor, Position destination, int weaponIndex, Integer weaponExchangedIndex) {
        mMoveAction = new MoveAction(playerColor, destination);
        mGrabAction = new GrabWeaponAction(weaponIndex, weaponExchangedIndex);
    }


    public MoveAction getMoveAction() {
        return mMoveAction;
    }

    public GrabAction getGrabAction() {
        return mGrabAction;
    }

    @Override
    public void perform(Game game) {
        mMoveAction.perform(game);
        mGrabAction.perform(game);
    }

    @Override
    public InvalidActionResponse getErrorResponse(Game game) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING);
        }

        // this action can be performed only by active player
        if (mMoveAction.getTarget() != game.getActivePlayer().getColor()) {
            return new MessageActionResponse(ActionResponseStrings.HACKED_MOVE);
        }

        Player player = game.getPlayerFromColor(mMoveAction.getTarget());

        InvalidActionResponse response = mGrabAction.getErrorMessageAtPos(game, mMoveAction.getDestination());
        if (response != null) {
            return response;
        }

        // check max possible moves in final frenzy status
        if (game.isFinalFrenzy()) {
            if (game.hasFirstPlayerDoneFinalFrenzy()) {
                return game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= 3 ?
                        null : new MessageActionResponse(ActionResponseStrings.ILLEGAL_TILE_DISTANCE);
            }
            else {
                return game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= 2 ?
                        null : new MessageActionResponse(ActionResponseStrings.ILLEGAL_TILE_DISTANCE);
            }

        }
        // check max moves in "normal" game status, it changes if player has tot damage
        else {
            return game.getBoard()
                    .getTileDistance(player.getPos(), mMoveAction.getDestination()) <= player.getMaxGrabDistance() ?
                    null : new MessageActionResponse(ActionResponseStrings.ILLEGAL_TILE_DISTANCE);
        }
    }

    @Override
    public boolean consumeAction() {
        return true;
    }
}
