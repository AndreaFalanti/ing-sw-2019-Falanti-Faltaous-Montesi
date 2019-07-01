package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.response.ActionResponseStrings;
import it.polimi.se2019.model.action.response.InvalidActionResponse;
import it.polimi.se2019.model.action.response.MessageActionResponse;

import java.util.Optional;

public class NewtonAction implements Action {
    private PlayerColor mTarget;
    private Position mDestination;
    private int mNewtonIndex;

    public NewtonAction(int index) {
        if (index < 0 || index >= 3) {
            throw new IllegalArgumentException("invalid powerUp index");
        }

        mNewtonIndex = index;
    }

    public NewtonAction(PlayerColor target, Position destination, int index) {
        this(index);

        mTarget = target;
        mDestination = destination;
    }

    public PlayerColor getTarget() {
        return mTarget;
    }

    public Position getDestination() {
        return mDestination;
    }

    public int getNewtonIndex() {
        return mNewtonIndex;
    }

    public void setTarget(PlayerColor target) {
        mTarget = target;
    }

    public void setDestination(Position destination) {
        mDestination = destination;
    }

    @Override
    public void perform(Game game) {
        Player player = game.getPlayerFromColor(mTarget);
        player.move(mDestination);
        player.discard(mNewtonIndex);
    }

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        PowerUpCard powerUpCard = game.getActivePlayer().getPowerUpCard(mNewtonIndex);

        if (powerUpCard == null) {
            return Optional.of(new MessageActionResponse("Power up used is null"));
        }
        if (game.getBoard().getTileDistance(game.getPlayerFromColor(mTarget).getPos(), mDestination) >= 3) {
            return Optional.of(new MessageActionResponse("Can't move a player more than 2 spaces"));
        }
        if (powerUpCard.getType() != PowerUpType.NEWTON) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.HACKED_MOVE));
        }

        return Optional.empty();
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
