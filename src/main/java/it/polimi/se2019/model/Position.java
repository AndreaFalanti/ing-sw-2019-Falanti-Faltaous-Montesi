package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Direction;

import java.util.Objects;

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
     */
    public Position (int x, int y) {
        mX = x;
        mY = y;
    }

    /**
     *
     * @param xAndY X and Y coordinate
     */
    public Position(int xAndY) {
        mX = xAndY;
        mY = xAndY;
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
     * Simple scalar multiplication
     * @param toMultiply scalar to multiply by
     * @return result of multiplication
     */
    public Position scalarMultiply(int toMultiply) {
        return new Position(mX * toMultiply, mY * toMultiply);
    }

    /**
     * Increments a position in a given direction {@code amount} times
     * @param incrementDirection direction of increment
     * @return the result of the increment
     */
    public Position directionalIncrement(Direction incrementDirection, int amount) {
        return add(incrementDirection.toPosition().scalarMultiply(amount));
    }

    /**
     * Increment a position once in a given direction
     * @param incrementDirection direction of increment
     * @return the result of the increment
     */
    public Position directionalIncrement(Direction incrementDirection) {
        return directionalIncrement(incrementDirection, 1);
    }

    /**
     * Subtracts {@code toSubtract} from {@code this} and returns the result
     * @param toSubtract position to subtract
     * @return result of sum
     */
    public Position subtract(final Position toSubtract) {
        return new Position(mX - toSubtract.getX(), mY - toSubtract.getY());
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

    @Override
    public int hashCode() {
        return Objects.hash(mX, mY);
    }

    /**
     * Clones {@code this}
     * @return the cloned position
     */
    public Position deepCopy() {
        return new Position(getX(), getY());
    }

    @Override
    public String toString() {
        return "[" + getX() + ", " + getY() + "]";
    }
}
