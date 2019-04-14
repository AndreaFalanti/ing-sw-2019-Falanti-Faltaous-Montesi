package it.polimi.se2019.model.board;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.Player;


public abstract class Tile {
    private TileColor mColor;

    @JsonAdapter(DoorsDeserializer.class)
    private int doors;

    public Tile finishDeepCopy(Tile childTile) {
        Tile result = childTile;

        result.mColor = mColor;

        return result;
    }

    public abstract Tile deepCopy();

    public abstract void grabObjects(Player player);

    public TileColor getColor() {
        return null;
    }

    public boolean[] getDoors() {
        return null;
    }

    public abstract String getTileType();
}
