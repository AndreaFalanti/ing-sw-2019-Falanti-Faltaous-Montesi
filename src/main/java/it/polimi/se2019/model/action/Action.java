package it.polimi.se2019.model.action;


import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.NotEnoughAmmoException;

public interface Action {
    void perform(Game game) throws NotEnoughAmmoException;

    boolean isValid(Game game);

    ResponseCode getCode();
}
