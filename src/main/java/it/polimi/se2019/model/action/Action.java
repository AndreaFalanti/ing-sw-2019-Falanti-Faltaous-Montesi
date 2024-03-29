package it.polimi.se2019.model.action;


import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.response.InvalidActionResponse;

import java.util.Optional;

/**
 * Common interface to all actions
 *
 * @author Andrea Falanti
 */
public interface Action {
    void perform(Game game);

    Optional<InvalidActionResponse> getErrorResponse(Game game);

    boolean consumeAction();

    boolean isComposite();

    boolean leadToAShootInteraction ();
}
