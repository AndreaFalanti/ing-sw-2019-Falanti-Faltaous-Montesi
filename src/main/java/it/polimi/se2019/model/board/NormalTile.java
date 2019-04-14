package it.polimi.se2019.model.board;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.Player;


public class NormalTile extends Tile {

    private AmmoCard mAmmoCard;

    public void setAmmoCard(AmmoCard value) {
    }

    @Override
    public NormalTile deepCopy() {
        NormalTile result = new NormalTile();

        result.mAmmoCard = mAmmoCard.deepCopy();

        return result;
    }

    @Override
    public void grabObjects(Player player) { }

    @Override
    public String getTileType() {
        return "normal";
    }
}
