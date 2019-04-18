package it.polimi.se2019.model;

public class Position {
    private int mX;
    private int mY;

    /**
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @throws IllegalArgumentException Thrown if at least one parameter is negative
     */
    public Position (int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("At least one value is negative");
        }

        mX = x;
        mY = y;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }
}
