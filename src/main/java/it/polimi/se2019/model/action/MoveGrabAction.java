package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

/**
 * Action for performing consecutively a move and a grab action
 *
 * @author Andrea Falanti
 */
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

    public void setGrabAction(GrabAction grabAction) {
        mGrabAction = grabAction;
    }

    @Override
    public void perform(Game game) {
        mMoveAction.perform(game);
        mGrabAction.perform(game);
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        // can't perform "costly" actions if there are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }

        // this action can be performed only by active player
        if (mMoveAction.getTarget() != game.getActivePlayer().getColor()) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.HACKED_MOVE));
        }

        Optional<InvalidActionResponse> response = mMoveAction.getErrorResponse(game);
        if (response.isPresent()) {
            return response;
        }

        Player player = game.getPlayerFromColor(mMoveAction.getTarget());

        response = mGrabAction.getErrorMessageAtPos(game, mMoveAction.getDestination());
        if (response.isPresent()) {
            return response;
        }

        int maxGrabDistance;

        // check max possible moves in different game states
        // check max moves in "normal" game status, it changes if player has tot damage
        if (!game.isFinalFrenzy()) {
            maxGrabDistance = player.getMaxGrabDistance();
        }
        // max moves in "final frenzy" game status are 3 if player is after (or is) first player
        else if (game.hasFirstPlayerDoneFinalFrenzy()) {
            maxGrabDistance = 3;
        }
        // max moves in "final frenzy" game status are 2 if player is before first player
        else {
            maxGrabDistance = 2;
        }

        return game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= maxGrabDistance ?
                Optional.empty() : Optional.of(new MessageActionResponse(ActionResponseStrings.ILLEGAL_TILE_DISTANCE));
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
        return false;
    }
}
