package it.polimi.se2019.model.board;

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
}
