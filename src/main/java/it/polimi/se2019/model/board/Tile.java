package it.polimi.se2019.model.board;

import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.board.serialization.DoorsDeserializer;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.util.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Tile extends Observable<Update> {
    private TileColor mColor = TileColor.BLUE;

    @JsonAdapter(DoorsDeserializer.class)
    private Integer mDoors = 0;

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

    @Override
    public int hashCode() {
        return Objects.hash(mColor, mDoors);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        Tile casted = (Tile) other;

        return mColor == casted.mColor &&
                mDoors.intValue() == casted.mDoors.intValue();
    }

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

    public List<Direction> getDoorsDirections() {
        List<Direction> result = new ArrayList<>();
        boolean[] doors = getDoors();

        if (doors[3]) result.add(Direction.NORTH);
        if (doors[2]) result.add(Direction.EAST);
        if (doors[1]) result.add(Direction.SOUTH);
        if (doors[0]) result.add(Direction.WEST);

        return result;
    }

    public abstract String getTileType();
}
