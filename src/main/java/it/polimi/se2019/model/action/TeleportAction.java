package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class TeleportAction implements Action {
    private Position mDestination;
    private int mTeleportIndex;

    public TeleportAction(int index) {
        if (index < 0 || index >= 3) {
            throw new IllegalArgumentException("invalid powerUp index");
        }

        mTeleportIndex = index;
    }

    public TeleportAction (Position destination, int index) {
        this(index);
        mDestination = destination;
    }

    public int getTeleportIndex() {
        return mTeleportIndex;
    }

    public void setDestination(Position destination) {
        mDestination = destination;
    }

    @Override
    public void perform(Game game) {
        Player player = game.getActivePlayer();
        player.move(mDestination);
        player.discard(mTeleportIndex);
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        PowerUpCard powerUpCard = game.getActivePlayer().getPowerUpCard(mTeleportIndex);

        if (powerUpCard == null) {
            return Optional.of(new MessageActionResponse("Power up used is null"));
        }
        return powerUpCard.getType() == PowerUpType.TELEPORT ?
                Optional.empty() : Optional.of(new MessageActionResponse(ActionResponseStrings.HACKED_MOVE));
    }

    @Override
    public boolean consumeAction() {
        return false;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public boolean leadToAShootInteraction() {
        return false;
    }
}
