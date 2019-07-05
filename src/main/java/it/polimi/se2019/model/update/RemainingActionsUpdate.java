package it.polimi.se2019.model.update;

/**
 * Update message with remaining actions info, sent to views
 *
 * @author Andrea Falanti
 */
public class RemainingActionsUpdate implements Update {
    private int mRemainingActions;

    public RemainingActionsUpdate(int remainingActions) {
        mRemainingActions = remainingActions;
    }

    public int getRemainingActions() {
        return mRemainingActions;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
