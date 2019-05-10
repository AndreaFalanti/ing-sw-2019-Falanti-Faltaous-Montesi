package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class MoveShootAction implements Action {
    private MoveAction mMoveAction;
    private ShootAction mShootAction;
    private ResponseCode mCode;
    private String message;

    public MoveShootAction (PlayerColor playerColor, Position destination) {
        mMoveAction = new MoveAction(playerColor, destination);
        mShootAction = new ShootAction();
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
    public boolean isValid(Game game) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            System.out.println("Max number of action reached");
            this.mCode = ResponseCode.NO_ACTION_LEFT;
            return false;
        }

        Player player = game.getPlayerFromColor(mMoveAction.getTarget());
        if (game.isFinalFrenzy()) {
            return (game.hasFirstPlayerDoneFinalFrenzy()) ?
                    game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) <= 2 :
                    game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) == 1;
        }
        else {
            return player.canMoveBeforeShooting()
                    && game.getBoard().getTileDistance(player.getPos(), mMoveAction.getDestination()) == 1;
        }
    }

    public ResponseCode getCode(){return mCode;}
}
