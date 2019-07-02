package it.polimi.se2019.model.board;

import com.google.gson.annotations.SerializedName;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.util.Observable;

import java.util.Objects;
import java.util.Set;

public abstract class Tile extends Observable<Update> {
    private TileColor mColor = TileColor.BLUE;
    private Position mPosition = null;
    @SerializedName("doors")
    private Set<Direction> mDoorDirections;

    protected Tile() {}

    protected Tile(TileColor color, Set<Direction> doors) {
        mColor = color;
        mDoorDirections = doors;
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    public abstract Tile deepCopy();
    public Tile finishDeepCopy(Tile childTile) {
        Tile result = childTile;

        result.mColor = mColor;
        result.mPosition = mPosition;
        result.mDoorDirections = mDoorDirections;

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mColor, mDoorDirections);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        Tile casted = (Tile) other;

        return mColor == casted.mColor &&
                mDoorDirections.equals(casted.mDoorDirections);
    }

    public TileColor getColor() {
        return mColor;
    }

    public boolean[] getDoors() {
        boolean[] result = new boolean[] {false, false, false, false};

        for (Direction direction : mDoorDirections) {
            switch (direction) {
                case NORTH:
                    result[0] = true;
                    break;
                case EAST:
                    result[1] = true;
                    break;
                case SOUTH:
                    result[2] = true;
                    break;
                case WEST:
                    result[3] = true;
                    break;
            }
        }

        return result;
    }

    public Set<Direction> getDoorsDirections() {
        return mDoorDirections;
    }

    public abstract String getTileType();
}
