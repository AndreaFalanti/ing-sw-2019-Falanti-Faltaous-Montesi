package it.polimi.se2019.model;


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
    public AmmoValue (int r, int y, int b) throws IllegalArgumentException {
        if (!isValueValid(r) || !isValueValid(y) || !isValueValid(b)) {
            throw new IllegalArgumentException();
        }
        mRed = r;
        mYellow = y;
        mBlue = b;
    }

    private boolean isValueValid (int value) {
        return value <= MAX_AMMO && value >= 0;
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

    public int getRed () {
        return mRed;
    }

    public int getYellow () {
        return mYellow;
    }

    public int getBlue () {
        return mBlue;
    }

    @Override
    public String toString() {
        return "AmmoValue{" +
                "mRed=" + mRed +
                ", mYellow=" + mYellow +
                ", mBlue=" + mBlue +
                '}';
    }
}
