package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Weapon;

import java.util.Arrays;
import java.util.stream.Collectors;


public class SpawnTile extends Tile {
    private Weapon[] mWeapons;


    public void addWeapon(Weapon value) {
    }

    public SpawnTile deepCopy() {
        SpawnTile result = new SpawnTile();

        result.mWeapons = (Weapon[]) Arrays.stream(mWeapons)
                .map(Weapon::deepCopy)
                .toArray();

        return (SpawnTile) finishDeepCopy(result);
    }

    @Override
    public void grabObjects(Player player) { }

    @Override
    public String getTileType() {
        return "spawn";
    }
}
