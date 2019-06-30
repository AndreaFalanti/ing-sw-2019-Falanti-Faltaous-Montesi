package it.polimi.se2019.model;


import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Constructs an AmmoValue by parsing a string
     * @param str textual representation of AmmoValue
     * @return result or parsing
     */
    public static Optional<AmmoValue> from(String str) {
        Matcher m = Pattern.compile("^(?!$)(?:(\\d)r)?(?:(\\d)y)?(?:(\\d)b)?$")
                .matcher(str);

        if (!m.matches())
            return Optional.empty();

        int red = Integer.parseInt(m.group(1) == null ? "0" : m.group(1));
        int yellow = Integer.parseInt(m.group(2) == null ? "0" : m.group(2));
        int blue = Integer.parseInt(m.group(3) == null ? "0" : m.group(3));

        return Optional.of(new AmmoValue(red, yellow, blue));
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

    /**
     * Check that ammo value is between 0 and MAX_AMMO
     * @param value value to check
     * @return true if in the interval, false otherwise
     */
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
     * @return This AmmoValue after addition
     */
    public AmmoValue add(AmmoValue value) {
        mRed += value.mRed;
        mYellow += value.mYellow;
        mBlue += value.mBlue;

        mRed = clampValue(mRed);
        mYellow = clampValue(mYellow);
        mBlue = clampValue(mBlue);

        return this;
    }

    /**
     * Subtract operation between AmmoValue
     * @param value AmmoValue to subtract
     * @throws NotEnoughAmmoException Thrown if any type of ammo of result is negative
     * @return This AmmoValue after subtract
     */
    public AmmoValue subtract(AmmoValue value) {
        return subtract(value, false);
    }

    public AmmoValue subtract(AmmoValue value, boolean allowNegativeValues) {
        mRed -= value.mRed;
        mYellow -= value.mYellow;
        mBlue -= value.mBlue;

        if (!allowNegativeValues && (mRed < 0 || mYellow < 0 || mBlue < 0)) {
            throw new NotEnoughAmmoException("Not enough ammo to pay cost");
        }

        return this;
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
