package it.polimi.se2019.model;

import java.util.*;


public class NormalTile extends Tile {

    private AmmoCard ammoCard;

    public void setAmmoCard(AmmoCard value) {
    }

    @Override
    public void grabObjects(Player player) { }

    @Override
    public String getTileType() {
        return "normal";
    }
}
