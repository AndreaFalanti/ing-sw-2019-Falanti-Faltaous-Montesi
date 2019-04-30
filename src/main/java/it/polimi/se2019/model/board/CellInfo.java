package it.polimi.se2019.model.board;

import java.util.Objects;

public class CellInfo {
    private boolean mVisible = false;
    private int mDistance = -1;

    // trivial constructors
    public CellInfo(boolean visible, int distance) {
        mVisible = visible;
        mDistance = distance;
    }
    public CellInfo(int distance) {
        mDistance = distance;
    }
    public CellInfo(boolean visible) {
        mVisible = visible;
    }

    // trivial getters and setters
    public boolean isVisible() {
        return mVisible;
    }

    public void toggleVisible() {
        mVisible = !mVisible;
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    // equals and hashcode
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;

        if (other == null || other.getClass() != getClass())
            return false;

        CellInfo casted = (CellInfo) other;

        return mVisible == casted.mVisible &&
                mDistance == casted.mDistance;
    }
    @Override
    public int hashCode() {
        return Objects.hash(mVisible, mDistance);
    }
}
