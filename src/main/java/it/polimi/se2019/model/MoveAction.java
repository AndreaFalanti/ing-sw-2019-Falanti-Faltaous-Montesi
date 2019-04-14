package it.polimi.se2019.model;

import java.util.*;


public class MoveAction extends Action {
    Position mDestination;

    public MoveAction(PlayerColor playerColor, Position destination) {
        super(playerColor);

        this.mDestination = destination;
    }

    @Override
    public void perform(Model model) { }

    @Override
    public boolean isValid(Model model) {
        return true;
    }
}
