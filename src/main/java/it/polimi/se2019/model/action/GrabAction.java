package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;

public interface GrabAction extends Action {
    InvalidActionResponse getErrorMessageAtPos(Game game, Position pos);
}