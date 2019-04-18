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

        if (other == null || getClass() != other.getClass())
            return false;

        return mColor == other.mColor &&
                mDoors.intValue() == other.mDoors.intValue();
    }

    public abstract void grabObjects(Player player);

    public TileColor getColor() {
        return mColor;
    }

    public boolean[] getDoors() {
        boolean[] doors = new boolean[4];
        for (int i = 3; i >= 0; i--) {
            //bitwise operation, 1 << i simply left shifts 1 of i positions to create correct masks.
            doors[i] = (mDoors & (1 << i)) != 0;
        }

        return doors;
    }

    public abstract String getTileType();
}
