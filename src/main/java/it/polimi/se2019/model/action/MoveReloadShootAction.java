package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class MoveReloadShootAction implements Action {
    private MoveShootAction mMoveShootAction;
    private ReloadAction mReloadAction;
    private ResponseCode mCode;
    private String message;

    public MoveReloadShootAction (PlayerColor playerColor, Position destination, int weaponIndex) {
        mMoveShootAction = new MoveShootAction(playerColor, destination);
        mReloadAction = new ReloadAction(weaponIndex);
    }

    public MoveShootAction getMoveShootAction() {
        return mMoveShootAction;
    }

    public ReloadAction getReloadAction() {
        return mReloadAction;
    }

    @Override
    public void perform(Game game) {
        mReloadAction.perform(game);
        mMoveShootAction.perform(game);
    }

    @Override
    public boolean isValid(Game game) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            System.out.println("Max number of action reached");
            this.mCode = ResponseCode.NO_ACTION_LEFT;
            return false;
        }
        return game.isFinalFrenzy() && mMoveShootAction.isValid(game) && mReloadAction.isValid(game);
    }

    @Override
    public boolean consumeAction() {
        return true;
    }

    public ResponseCode getCode(){return mCode;}
}
