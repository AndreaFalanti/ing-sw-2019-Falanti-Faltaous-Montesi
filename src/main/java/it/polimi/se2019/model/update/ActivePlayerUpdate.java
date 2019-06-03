package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

public class ActivePlayerUpdate implements Update {
    private PlayerColor mPlayerColor;
    private int mRemainingActions;
    private int mTurnNumber;

    public ActivePlayerUpdate(PlayerColor playerColor, int remainingActions, int turnNumber) {
        mPlayerColor = playerColor;
        mRemainingActions = remainingActions;
        mTurnNumber = turnNumber;
    }

    public PlayerColor getPlayerColor() {
        return mPlayerColor;
    }

    public int getRemainingActions() {
        return mRemainingActions;
    }

    public int getTurnNumber() {
        return mTurnNumber;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
