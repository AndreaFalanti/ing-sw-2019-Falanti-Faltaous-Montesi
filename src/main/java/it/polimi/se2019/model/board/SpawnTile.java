package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Weapon;


public class SpawnTile extends Tile {
    private Weapon[] weapons;


    public void addWeapon(Weapon value) {
    }

    @Override
    public void grabObjects(Player player) { }

    @Override
    public String getTileType() {
        return "spawn";
    }
}
