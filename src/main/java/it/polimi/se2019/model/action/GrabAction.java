package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.InvalidActionResponse;

import java.util.Optional;

/**
 * Interface used exclusively by grab actions
 *
 * @author Andrea Falanti
 */
public interface GrabAction extends Action {
    Optional<InvalidActionResponse> getErrorMessageAtPos(Game game, Position pos);
}