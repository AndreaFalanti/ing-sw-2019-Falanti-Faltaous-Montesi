package it.polimi.se2019.model.action;


import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;

public interface Action {
    void perform(Game game);

    InvalidActionResponse getErrorResponse(Game game);

    boolean consumeAction();
}
