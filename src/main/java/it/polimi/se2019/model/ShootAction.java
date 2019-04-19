package it.polimi.se2019.model;


public class ShootAction extends Action {
    Position mDestination;

    public ShootAction(PlayerColor playerColor, Position destination) {
        super(playerColor);

        this.mDestination = destination;
    }

    @Override
    public void perform(Game game) {}

    @Override
    public boolean isValid(Game game) { return true; }

}