package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;

import java.util.ArrayList;


public abstract class Weapon {


    private boolean loaded;

    private AmmoValue grabCost;

    private AmmoValue reloadCost;

    public abstract Weapon deepCopy();

    public abstract void shoot(Player shooter);

    public String getName() {
        return "";
    }

    public AmmoValue getGrabCost() {
        return null;
    }

    public AmmoValue getReloadCost() {
        return null;
    }

    public abstract ArrayList<Player> getTargets(Player shooter, Board board);

    public boolean isLoaded() {
        return false;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
