package it.polimi.se2019.model;


import java.util.Objects;

public class AmmoValue {
    private int mRed;
    private int mYellow;
    private int mBlue;

    public static final int MAX_AMMO = 3;

    public AmmoValue () {
        mRed = 0;
        mYellow = 0;
        mBlue = 0;
    }

    /**
     *
     * @param r Red ammo value
     * @param y Yellow ammo value
     * @param b Blue ammo value
     * @throws IllegalArgumentException Thrown if any ammo value is negative or above MAX_AMMO value
     */
    public AmmoValue (int r, int y, int b) {
        if (!isValueValid(r) || !isValueValid(y) || !isValueValid(b)) {
            throw new IllegalArgumentException();
        }
        mRed = r;
        mYellow = y;
        mBlue = b;
    }

    public int getRed () {
        return mRed;
    }

    public int getYellow () {
        return mYellow;
    }

    public int getBlue () {
        return mBlue;
    }

    private boolean isValueValid (int value) {
        return value <= MAX_AMMO && value >= 0;
    }

    public AmmoValue deepCopy() {
        AmmoValue result = new AmmoValue();

        result.mRed = mRed;
        result.mYellow = mYellow;
        result.mBlue = mBlue;

        return result;
    }

    /**
     * Add operation between AmmoValue
     * @param value AmmoValue to add
     */
    public void add(AmmoValue value) {
        mRed += value.mRed;
        mYellow += value.mYellow;
        mBlue += value.mBlue;

        mRed = clampValue(mRed);
        mYellow = clampValue(mYellow);
        mBlue = clampValue(mBlue);
    }

    /**
     * Subtract operation between AmmoValue
     * @param value AmmoValue to subtract
     * @throws NotEnoughAmmoException Thrown if any type of ammo of result is negative
     */
    public void subtract(AmmoValue value) throws NotEnoughAmmoException {
        mRed -= value.mRed;
        mYellow -= value.mYellow;
        mBlue -= value.mBlue;

        if (mRed < 0 || mYellow < 0 || mBlue < 0) {
            throw new NotEnoughAmmoException("Not enough ammo to pay cost");
        }
    }

    /**
     * Verify that every ammo type is bigger or equal than parameter passed.
     * @param value AmmoValue to confront with
     * @return True if every ammo type is >= than the other respective one, false otherwise
     */
    public boolean isBiggerOrEqual (AmmoValue value) {
        return mRed >= value.mRed && mYellow >= value.mYellow && mBlue >= value.mBlue;
    }

    /**
     * Clamp ammo value if exceed max defined value
     * @param value Ammo value to clamp
     * @return  Value clamped
     */
    private int clampValue (int value) {
        if (value > MAX_AMMO) {
            value = MAX_AMMO;
        }

        return value;
    }

    @Override
    public String toString() {
        return "AmmoValue{" +
                "mRed=" + mRed +
                ", mYellow=" + mYellow +
                ", mBlue=" + mBlue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmmoValue ammoValue = (AmmoValue) o;
        return mRed == ammoValue.mRed &&
                mYellow == ammoValue.mYellow &&
                mBlue == ammoValue.mBlue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mRed, mYellow, mBlue);
    }
}
