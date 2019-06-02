package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

public class PlayerMarksUpdate implements Update {
    private PlayerColor mTargetPlayerColor;
    private int mMarks;
    private PlayerColor mShooterPlayerColor;

    public PlayerMarksUpdate(PlayerColor targetPlayerColor, int marks, PlayerColor shooterPlayerColor) {
        mTargetPlayerColor = targetPlayerColor;
        mMarks = marks;
        mShooterPlayerColor = shooterPlayerColor;
    }

    public PlayerColor getTargetPlayerColor() {
        return mTargetPlayerColor;
    }

    public int getMarks() {
        return mMarks;
    }

    public PlayerColor getShooterPlayerColor() {
        return mShooterPlayerColor;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
