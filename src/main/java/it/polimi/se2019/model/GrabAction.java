package it.polimi.se2019.model;


public class GrabAction implements Action {
    private boolean mWeaponGrab;
    private int mWeaponIndex;

    public GrabAction() {
        mWeaponGrab = false;
        mWeaponIndex = -1;
    }

    public boolean isWeaponGrab() {
        return mWeaponGrab;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    @Override
    public void perform(Game game) {
        Position playerPos = game.getActivePlayer().getPos();
        //game.getBoard().getTileAt(playerPos).
    }

    @Override
    public boolean isValid(Game game) { return true; }
}