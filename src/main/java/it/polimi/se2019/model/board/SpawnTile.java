package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Weapon;

import java.util.Arrays;


public class SpawnTile extends Tile {
    private Weapon[] mWeapons;


    public void addWeapon(Weapon value) {
        // TODO: implement
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

    @Override
    public void grabObjects(Player player) {
        // TODO: implement
    }

    @Override
    public String getTileType() {
        return "spawn";
    }
}
