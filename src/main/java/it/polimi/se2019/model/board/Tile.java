package it.polimi.se2019.model.board;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.board.serialization.DoorsDeserializer;

public abstract class Tile {
    private TileColor mColor;

    @JsonAdapter(DoorsDeserializer.class)
    private Integer mDoors;

    protected Tile() {}

    protected Tile(TileColor color, int doors) {
        mColor = color;
        mDoors = doors;
    }

    public abstract Tile deepCopy();
    public Tile finishDeepCopy(Tile childTile) {
        Tile result = childTile;

        result.mColor = mColor;

        return result;
    }

    public boolean equals(Tile other) {
        if (this == other)
            return true;

        if (getClass() != other.getClass())
            return false;

        return mColor == other.mColor &&
                mDoors == other.mDoors;
    }

    public abstract void grabObjects(Player player);

    public TileColor getColor() {
        return null;
    }

    public boolean[] getDoors() {
        return null;
    }

    public abstract String getTileType();
}
