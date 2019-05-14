package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TODO: add doc
// TODO: add implementation
public class WeaponAction implements Action {
    private final List<Action> mActions = new ArrayList();

    public WeaponAction(List<Action> actions) {
        mActions.addAll(actions);
    }

    public WeaponAction(Action... actions) {
       mActions.addAll(Arrays.stream(actions).collect(Collectors.toList()));
    }

    @Override
    public void perform(Game game) {
        mActions.forEach(action -> action.perform(game));
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
