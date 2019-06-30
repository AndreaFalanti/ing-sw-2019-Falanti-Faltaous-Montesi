package it.polimi.se2019.model.action;


import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.response.InvalidActionResponse;

import java.util.Optional;

public interface Action {
    void perform(Game game);

    Optional<InvalidActionResponse> getErrorResponse(Game game);

    boolean consumeAction();

    boolean isComposite();
}
