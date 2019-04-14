package it.polimi.se2019.model;

import java.util.*;


public class ShootAction extends Action {
    Position mDestination;

    public ShootAction(PlayerColor playerColor, Position destination) {
        super(playerColor);

        this.mDestination = destination;
    }

    @Override
    public void perform(Model model) {}

    @Override
    public boolean isValid(Model model) { return true; }

}