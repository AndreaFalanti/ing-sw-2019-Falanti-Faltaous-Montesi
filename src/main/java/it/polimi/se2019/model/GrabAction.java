package it.polimi.se2019.model;


public class GrabAction extends Action {
    Position mDestination;

    public GrabAction(PlayerColor playerColor, Position destination) {
        super(playerColor);

        this.mDestination = destination;
    }
    @Override
    public void perform(Game game) {}

    @Override
    public boolean isValid(Game game) { return true; }

}