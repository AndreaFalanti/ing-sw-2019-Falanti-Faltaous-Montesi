package it.polimi.se2019.model;


public abstract class Action {

    private PlayerColor mPlayerColor;
    private Position mPos;


    public Action(PlayerColor playerColor) {
        this.mPlayerColor = playerColor;
    }

    public abstract void perform(Game game);

    public abstract boolean isValid(Game game);

    public PlayerColor getPlayer() {
        return null;
    }

    public Position getPos() {
        return null;
    }

}
