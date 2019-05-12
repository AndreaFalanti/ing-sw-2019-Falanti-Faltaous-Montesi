package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;

public interface GrabAction extends Action {
    boolean isValidAtPos(Game game, Position pos);
}