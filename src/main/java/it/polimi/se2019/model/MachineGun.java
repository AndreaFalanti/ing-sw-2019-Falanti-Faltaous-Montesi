package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;

import java.util.ArrayList;


public class MachineGun extends Weapon {
    private String name;

    public MachineGun(String nameW){
        this.name = nameW;
    }
    public void shoot(Player shooter) { }

    public ArrayList<Player> getTargets(Player shooter, Board board) {
        return null;
    }

    @Override
    public Weapon deepCopy() {
        return null;
    }
}
