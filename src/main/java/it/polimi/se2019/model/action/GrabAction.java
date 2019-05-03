package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;

public interface GrabAction {
    void perform(Game game);

    boolean isValid(Game game);
}