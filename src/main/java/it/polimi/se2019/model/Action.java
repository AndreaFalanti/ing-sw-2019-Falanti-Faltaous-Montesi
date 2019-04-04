package it.polimi.se2019.model;

import java.util.*;


public abstract class Action {

    private PlayerColor player;

    private Position pos;



    public abstract void perform(Model model);

    public abstract boolean isValid(Model model);

    public PlayerColor getPlayer() {
        return null;
    }

    public Position getPos() {
        return null;
    }

}
