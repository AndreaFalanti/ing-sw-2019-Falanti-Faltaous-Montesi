package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.model.weapon.behaviour.Expression;

import java.util.ArrayList;

@Deprecated
public class MachineGun extends Weapon {
    public MachineGun(String name) {
        super(null);
    }
    public MachineGun(Expression behaviour) {
        super(behaviour);
    }
}
