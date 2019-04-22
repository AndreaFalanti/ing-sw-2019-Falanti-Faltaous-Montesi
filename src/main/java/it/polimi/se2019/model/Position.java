package it.polimi.se2019.model;

public class Position {
    // coordinates
    private int mX;
    private int mY;

    // trivial getters
    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

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

    /**
     * Adds 2 positions together and returns the result
     * @param toAdd position to add
     * @return result of sum
     */
    public Position add(final Position toAdd) {
        return new Position(mX + toAdd.getX(), mY + toAdd.getY());
    }

    /**
     * Checks equality
     * @param other position to check equality against
     * @return true if {@code this} and {@code other} are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        Position casted = (Position) other;

        return mX == casted.mX && mY == casted.mY;
    }
}
