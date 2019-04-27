package it.polimi.se2019.model;


public class MoveAction implements Action {
    private PlayerColor mTarget;
    private Position mDestination;
    private boolean mTeleport;

    public MoveAction(PlayerColor playerColor, Position destination) {
        mTarget = playerColor;
        mDestination = destination;
        mTeleport = false;
    }

    public MoveAction(PlayerColor playerColor, Position destination, boolean isTeleport) {
        mTarget = playerColor;
        mDestination = destination;
        mTeleport = isTeleport;
    }

    public PlayerColor getTarget() {
        return mTarget;
    }

    public Position getDestination() {
        return mDestination;
    }

    public boolean isTeleport() {
        return mTeleport;
    }

    @Override
    public void perform(Game game) {
        game.getPlayerFromColor(mTarget).move(mDestination);
    }

    @Override
    public boolean isValid(Game game) {
        if (mTeleport) {
            return true;
        }
        else {
            Position playerPos = game.getPlayerFromColor(mTarget).getPos();
            return game.getBoard().getTileDistance(playerPos, mDestination) <= 3;
        }
    }
}
