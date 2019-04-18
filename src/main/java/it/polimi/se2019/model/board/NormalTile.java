package it.polimi.se2019.model.board;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.Player;


public class NormalTile extends Tile {

    private AmmoCard mAmmoCard;

    public NormalTile() {
        super();
    }

    public NormalTile(TileColor color, int doors) {
        super(color, doors);
    }

    public void setAmmoCard(AmmoCard value) {
    }

    @Override
    public NormalTile deepCopy() {
        NormalTile result = new NormalTile();

        // TODO: uncomment when AmmoCard is implemented
        // result.mAmmoCard = mAmmoCard.deepCopy();

        return result;
    }

    public boolean equals(Tile other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        if (!super.equals(other))
            return false;

        NormalTile casted = (NormalTile) other;

        if (mAmmoCard == null) {
            return casted.mAmmoCard == null;
        }

        return mAmmoCard.equals(casted.mAmmoCard);
    }

    @Override
    public void grabObjects(Player player) { }

    @Override
    public String getTileType() {
        return "normal";
    }
}
