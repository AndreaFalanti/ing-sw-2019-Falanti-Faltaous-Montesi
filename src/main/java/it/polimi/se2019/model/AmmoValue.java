package it.polimi.se2019.model;

import java.util.*;


public class AmmoValue {
    private int red;
    private int yellow;
    private int blue;

    public static final int MAX_AMMO = 3;

    public AmmoValue () {
        red = 0;
        yellow = 0;
        blue = 0;
    }

    public AmmoValue (int r, int y, int b) {
        red = r;
        yellow = y;
        blue = b;
    }

    /**
     * Add operation between AmmoValue
     * @param value AmmoValue to add
     */
    public void add(AmmoValue value) {
        red += value.red;
        yellow += value.yellow;
        blue += value.blue;

        red = clampValue(red);
        yellow = clampValue(yellow);
        blue = clampValue(blue);
    }

    /**
     * Subtract operation between AmmoValue
     * @param value AmmoValue to subtract
     * @throws NotEnoughAmmoException Thrown if any type of ammo is negative
     */
    public void subtract(AmmoValue value) throws NotEnoughAmmoException {
        red -= value.red;
        yellow -= value.yellow;
        blue -= value.blue;

        if (red < 0 || yellow < 0 || blue < 0) {
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
        return red;
    }

    public int getYellow () {
        return yellow;
    }

    public int getBlue () {
        return blue;
    }

}
