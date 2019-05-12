package it.polimi.se2019.model.board;

import it.polimi.se2019.model.weapon.Weapon;

import java.util.Arrays;


public class SpawnTile extends Tile {
    private Weapon[] mWeapons = new Weapon[MAX_WEAPONS];
    public static final int MAX_WEAPONS = 3;

    /**
     * Add {@code value} to first null slot in spawn's weapon array
     * @param value Weapon to add
     */
    public void addWeapon(Weapon value) {
        for (int i = 0; i < mWeapons.length; i++) {
            if (mWeapons[i] == null) {
                mWeapons[i] = value;
                return;
            }
        }
    }

    public SpawnTile deepCopy() {
        SpawnTile result = new SpawnTile();

        result.mWeapons = (Weapon[]) Arrays.stream(mWeapons)
                .map(Weapon::deepCopy)
                .toArray();

        return (SpawnTile) finishDeepCopy(result);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(mWeapons);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null ||  getClass() != other.getClass())
            return false;

        if (!super.equals(other))
            return false;

        SpawnTile casted = (SpawnTile) other;

        return Arrays.equals(mWeapons,  casted.mWeapons);
    }

    /**
     * Get weapon at selected index
     * @param index Index of weapon to return
     * @return Weapon get at index position
     */
    public Weapon getWeapon (int index) {
        return mWeapons[index];
    }

    public Weapon[] getWeapons() {
        return mWeapons;
    }

    /**
     * Grab weapon at selected index from spawn tile
     * @param index Index of weapon to grab
     * @return Weapon grabbed
     */
    public Weapon grabWeapon(int index) {
        Weapon grabbedCard = mWeapons[index];
        mWeapons[index] = null;
        return grabbedCard;
    }

    @Override
    public String getTileType() {
        return "spawn";
    }
}
