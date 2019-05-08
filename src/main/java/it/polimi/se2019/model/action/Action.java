package it.polimi.se2019.model.action;


import it.polimi.se2019.model.Game;

public interface Action {
    void perform(Game game);

    boolean isValid(Game game);
}
