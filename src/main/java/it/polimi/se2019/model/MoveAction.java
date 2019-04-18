package it.polimi.se2019.model;


public class MoveAction extends Action {
    Position mDestination;

    public MoveAction(PlayerColor playerColor, Position destination) {
        super(playerColor);

        this.mDestination = destination;
    }

    @Override
    public void perform(Game game) { }

    @Override
    public boolean isValid(Game game) {
        return true;
    }
}
