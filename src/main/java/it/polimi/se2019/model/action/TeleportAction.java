package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.PowerUpCard;

public class TeleportAction implements Action {
    private Position mDestination;
    private int mTeleportIndex;

    public TeleportAction (Position destination, int index) {
        if (index < 0 || index >= 3) {
            throw new IllegalArgumentException("invalid powerUp index");
        }

        mDestination = destination;
        mTeleportIndex = index;
    }

    public int getTeleportIndex() {
        return mTeleportIndex;
    }

    @Override
    public void perform(Game game) {
        Player player = game.getActivePlayer();
        player.move(mDestination);
        player.discard(mTeleportIndex);
    }

    @Override
    public boolean isValid(Game game) {
        PowerUpCard powerUpCard = game.getActivePlayer().getPowerUpCard(mTeleportIndex);
        return powerUpCard != null && powerUpCard.getName().equals("Teleport");
    }
}
