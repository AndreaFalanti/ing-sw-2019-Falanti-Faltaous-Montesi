package it.polimi.se2019.model;

import java.util.*;


public class GrabAction extends Action {
    Position mDestination;

    public GrabAction(PlayerColor playerColor, Position destination) {
        super(playerColor);

        this.mDestination = destination;
    }
    @Override
    public void perform(Model model) {}

    @Override
    public boolean isValid(Model model) { return true; }

}