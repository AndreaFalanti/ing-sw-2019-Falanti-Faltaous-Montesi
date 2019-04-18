package it.polimi.se2019.model;

import java.util.*;


public abstract class Weapon {


    private boolean loaded;

    private AmmoValue grabCost;

    private AmmoValue reloadCost;

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
}
