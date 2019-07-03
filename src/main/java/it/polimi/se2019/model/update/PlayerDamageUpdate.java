package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;

public class PlayerDamageUpdate implements Update {
    private PlayerColor mDamagedPlayerColor;
    private PlayerColor[] mDamageTaken;

    public PlayerDamageUpdate(PlayerColor damagedPlayerColor, PlayerColor[] damageTaken) {
        mDamagedPlayerColor = damagedPlayerColor;
        mDamageTaken = damageTaken;
    }

    public PlayerColor getDamagedPlayerColor() {
        return mDamagedPlayerColor;
    }

    public PlayerColor[] getDamageTaken() {
        return mDamageTaken;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
