package it.polimi.se2019.model;


public interface Action {
    public abstract void perform(Game game);

    public abstract boolean isValid(Game game);
}
