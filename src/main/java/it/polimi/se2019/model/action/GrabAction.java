package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;

public interface GrabAction extends Action {
    void perform(Game game);

    boolean isValid(Game game);
}