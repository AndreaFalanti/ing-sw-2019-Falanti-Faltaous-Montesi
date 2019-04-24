package it.polimi.se2019.model.board;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.Player;

import java.util.Objects;


public class NormalTile extends Tile {

    // normal tiles contain an ammo card
    private AmmoCard mAmmoCard;

    /**
     * Constructs empty BLUE tile with no ammo card
     */
    public NormalTile() {
        super();
    }

    /**
     * Constructs empty tile of given color with no ammo card
     * @param color color of constructed tile
     */
    public NormalTile(TileColor color) {
        super(color, 0);
    }

    /**
     * Constructs empty tile of given color and doors with no ammo card
     * @param color color of constructed tile
     */
    public NormalTile(TileColor color, int doors) {
        super(color, doors);
    }

    /**
     *
     * @param value ammo card to set
     */
    public void setAmmoCard(AmmoCard value) {
        // TODO: implement
    }

    /**
     *
     * @return clone of {@code this}
     */
    @Override
    public NormalTile deepCopy() {
        NormalTile result = new NormalTile();

        // TODO: uncomment when AmmoCard is implemented
        // result.mAmmoCard = mAmmoCard.deepCopy();

        return (NormalTile) finishDeepCopy(this);
    }

    /**
     *
     * @return hashcode associated to {@code this}
     */
    @Override
    public int hashCode() {
        return Objects.hash(mAmmoCard);
    }

    /**
     *
     * @param other object compared to {@code this}
     * @return true if {@code other} is equal to {@code this}
     */
    @Override
    public boolean equals(Object other) {
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

        return true; // TODO: implement equals in AmmoCard properly
                     // return mAmmoCard.equals(casted.mAmmoCard);
    }

    // TODO: add doc
    @Override
    public void grabObjects(Player player) { }

    /**
     *
     * @return string representation of {@code this}'s tile type
     */
    @Override
    public String getTileType() {
        return "normal";
    }
}
