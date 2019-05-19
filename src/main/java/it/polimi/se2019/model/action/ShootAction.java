package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.responses.ActionResponseStrings;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;
import it.polimi.se2019.model.action.responses.MessageActionResponse;

import java.util.Optional;

public class ShootAction implements Action {
    public ShootAction () {
    }

    @Override
    public void perform(Game game) {}

    @Override
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }

        return Optional.empty();
    }

    @Override
    public boolean consumeAction() {
        return true;
    }
}