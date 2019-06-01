package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

public class PlayerDamageUpdate implements Update {
    private PlayerColor mDamagedPlayerColor;
    private int mDamageTaken;
    private PlayerColor mShooterPlayerColor;

    public PlayerDamageUpdate(PlayerColor damagedPlayerColor, int damageTaken, PlayerColor shooterPlayerColor) {
        mDamagedPlayerColor = damagedPlayerColor;
        mDamageTaken = damageTaken;
        mShooterPlayerColor = shooterPlayerColor;
    }

    public PlayerColor getDamagedPlayerColor() {
        return mDamagedPlayerColor;
    }

    public int getDamageTaken() {
        return mDamageTaken;
    }

    public PlayerColor getShooterPlayerColor() {
        return mShooterPlayerColor;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
