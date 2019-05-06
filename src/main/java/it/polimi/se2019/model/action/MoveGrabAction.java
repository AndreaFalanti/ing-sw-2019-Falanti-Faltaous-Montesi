package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class MoveGrabAction implements Action{
    private MoveAction mMoveAction;
    private GrabAction mGrabAction;

    public MoveGrabAction (PlayerColor playerColor, Position destination) {
        mMoveAction = new MoveAction(playerColor, destination);
        mGrabAction = new GrabAmmoAction();
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
        if (game.getRemainingActions() == 0) {
            return false;
        }

        Player player = game.getPlayerFromColor(mMoveAction.getTarget());
        if (game.isFinalFrenzy()) {
            return (game.hasFirstPlayerDoneFinalFrenzy()) ?
                    game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= 3 :
                    game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= 2;
        }
        else {
            return game.getBoard()
                    .getTileDistance(player.getPos(), mMoveAction.getDestination()) <= player.getMaxGrabDistance();
        }
    }
}
