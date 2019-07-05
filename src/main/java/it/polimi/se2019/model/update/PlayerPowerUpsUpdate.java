package it.polimi.se2019.model.update;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.PowerUpCard;

/**
 * Update message with player's powerUps info, sent to views
 *
 * @author Andrea Falanti
 */
public class PlayerPowerUpsUpdate implements Update {
    private PlayerColor mPlayerColor;
    private PowerUpCard[] mPowerUpCards;

    public PlayerPowerUpsUpdate(PlayerColor playerColor, PowerUpCard[] powerUpCards) {
        mPlayerColor = playerColor;
        mPowerUpCards = powerUpCards;
    }

    public PlayerColor getPlayerColor() {
        return mPlayerColor;
    }

    public PowerUpCard[] getPowerUpCards() {
        return mPowerUpCards;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
