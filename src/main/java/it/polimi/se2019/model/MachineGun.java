package it.polimi.se2019.model;

import java.util.*;


public class MachineGun extends Weapon {
    private String name;

    public MachineGun(String nameW){
        this.name = nameW;
    }
    public void shoot(Player shooter) { }

    public ArrayList<Player> getTargets(Player shooter, Board board) {
        return null;
    }

}
