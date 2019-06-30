package it.polimi.se2019.model.update;

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
