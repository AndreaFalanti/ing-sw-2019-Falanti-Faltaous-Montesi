package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

public class MoveReloadShootAction implements Action {
    private MoveShootAction mMoveShootAction;
    private ReloadAction mReloadAction;

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
        return game.isFinalFrenzy() && mMoveShootAction.isValid(game) && mReloadAction.isValid(game);
    }
}
