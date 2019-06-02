package it.polimi.se2019.model;


import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Damage {
    private static Pattern PATTERN  = Pattern.compile("^(?!$)(?:(\\d+)d)?(?:(\\d+)m)?$");

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
    public Damage (int dmg, int marks) {
        if (dmg < 0 || marks < 0) {
            throw new IllegalArgumentException ("At least one value is negative");
        }
        mDamage = dmg;
        mMarksNum = marks;
    }

    /**
     * Constructs an AmmoValue by parsing a string
     * @param str textual representation of AmmoValue
     * @return result or parsing (empty optional if string cannot be parsed)
     */
    public static Optional<Damage> from(String str) {
        Matcher m = PATTERN.matcher(str);

        if (!m.matches())
            return Optional.empty();

        int damage = Integer.parseInt(m.group(1) == null ? "0" : m.group(1));
        int marks = Integer.parseInt(m.group(2) == null ? "0" : m.group(2));

        return Optional.of(new Damage(damage, marks));
    }

    public int getDamage() {
        return mDamage;
    }

    public int getMarksNum() {
        return mMarksNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Damage damage = (Damage) o;

        return mDamage == damage.mDamage &&
                mMarksNum == damage.mMarksNum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mDamage, mMarksNum);
    }

    @Override
    public String toString() {
        return "Damage{" +
                "mDamage=" + mDamage +
                ", mMarksNum=" + mMarksNum +
                '}';
    }
}
