package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;

public class ShootAction implements Action {
    private ResponseCode mCode;
    private String message;

    public ShootAction () {
    }

    @Override
    public void perform(Game game) {}

    @Override
    public boolean isValid(Game game) {
        if (game.getRemainingActions() == 0) {
            System.out.println("Max number of action reached");
            this.mCode = ResponseCode.NO_ACTION_LEFT;
            return false;
        }
        return true;
    }

    public ResponseCode getCode(){return mCode;}

}