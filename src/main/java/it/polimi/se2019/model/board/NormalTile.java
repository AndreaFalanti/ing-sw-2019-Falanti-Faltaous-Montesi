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

    public boolean equals(Tile other) {
        if (this == other)
            return true;

        if (getClass() != other.getClass())
            return false;

        if (!super.equals(other))
            return false;

        NormalTile casted = (NormalTile) other;

        return mAmmoCard.equals(casted.mAmmoCard);
    }

    @Override
    public void grabObjects(Player player) { }

    @Override
    public String getTileType() {
        return "normal";
    }
}
