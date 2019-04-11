package it.polimi.se2019.model.board;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.Player;


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
