package it.polimi.se2019.model;

import java.util.*;


public abstract class Tile {
    private TileColor color;

    private boolean[] doors;


    public abstract void grabObjects(Player player);

    public TileColor getColor() {
        return null;
    }

    public boolean[] getDoors() {
        return null;
    }

    public abstract String getTileType();
}
