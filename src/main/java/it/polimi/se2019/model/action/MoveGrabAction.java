package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class MoveGrabAction implements Action {
    private MoveAction mMoveAction;
    private GrabAction mGrabAction;
    private ResponseCode mCode;
    private String message;

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
    public boolean isValid(Game game) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            System.out.println("Max number of action reached");
            this.mCode = ResponseCode.NO_ACTION_LEFT;
            return false;
        }

        // this action can be performed only by active player
        if (mMoveAction.getTarget() != game.getActivePlayer().getColor()) {
            this.mCode = ResponseCode.PERFORMABLE_BY_ACTIVE_PLAYER;
            return false;
        }

        Player player = game.getPlayerFromColor(mMoveAction.getTarget());

        if (!mGrabAction.isValidAtPos(game, mMoveAction.getDestination())) {
            return false;
        }

        // check max possible moves in final frenzy status
        if (game.isFinalFrenzy()) {
            return (game.hasFirstPlayerDoneFinalFrenzy()) ?
                    game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= 3 :
                    game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= 2;
        }
        // check max moves in "normal" game status, it changes if player has tot damage
        else {
            return game.getBoard()
                    .getTileDistance(player.getPos(), mMoveAction.getDestination()) <= player.getMaxGrabDistance();
        }
    }

    @Override
    public boolean consumeAction() {
        return true;
    }

    public ResponseCode getCode(){return mCode;}
}
