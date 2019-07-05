package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

/**
 * Update message with turn info, sent to views
 *
 * @author Andrea Falanti
 */
public class ActivePlayerUpdate implements Update {
    private PlayerColor mPlayerColor;
    private int mTurnNumber;
    private boolean mFinalFrenzy;

    public ActivePlayerUpdate(PlayerColor playerColor, int turnNumber, boolean finalFrenzy) {
        mPlayerColor = playerColor;
        mTurnNumber = turnNumber;
        mFinalFrenzy = finalFrenzy;
    }

    public PlayerColor getPlayerColor() {
        return mPlayerColor;
    }

    public int getTurnNumber() {
        return mTurnNumber;
    }

    public boolean isFinalFrenzy() {
        return mFinalFrenzy;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
