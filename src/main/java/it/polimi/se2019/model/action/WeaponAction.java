package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;

import java.util.List;

// TODO: add doc
// TODO: add implementation
public class WeaponAction implements Action {
    public WeaponAction(List<Action> actions) {

    }

    public WeaponAction(Action... actions) {

    }

    @Override
    public void perform(Game game) {

    }

    @Override
    public InvalidActionResponse getErrorResponse(Game game) {
        return null;
    }

    @Override
    public boolean consumeAction() {
        return false;
    }
}
