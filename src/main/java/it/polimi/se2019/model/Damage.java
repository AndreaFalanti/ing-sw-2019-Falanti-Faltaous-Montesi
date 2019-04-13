package it.polimi.se2019.model;


public class Damage {
    private int mDamage;
    private int mMarksNum;

    public Damage () {
        mDamage = 0;
        mMarksNum = 0;
    }

    /**
     *
     * @param dmg Damage value
     * @param marks Number of marks applied
     * @throws IllegalArgumentException Thrown if dmg or marks are negative
     */
    public Damage (int dmg, int marks) throws IllegalArgumentException {
        if (dmg < 0 || marks < 0) {
            throw new IllegalArgumentException ();
        }
        mDamage = dmg;
        mMarksNum = marks;
    }

    public int getDamage() {
        return mDamage;
    }

    public int getMarksNum() {
        return mMarksNum;
    }
}
