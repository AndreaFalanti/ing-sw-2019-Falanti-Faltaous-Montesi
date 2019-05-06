package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;

public class ShootAction implements Action {

    public ShootAction () {
    }

    @Override
    public void perform(Game game) {}

    @Override
    public boolean isValid(Game game) {
        if (game.getRemainingActions() == 0) {
            return false;
        }
        return true;
    }

}