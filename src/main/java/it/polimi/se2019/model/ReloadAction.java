package it.polimi.se2019.model;

public class ReloadAction implements Action {
    private int mWeaponIndex;

    public ReloadAction (int weaponIndex) {
        if (weaponIndex < 0 || weaponIndex >= 3) {
            throw new IllegalArgumentException("Illegal weapon index in Reload action");
        }
        mWeaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return mWeaponIndex;
    }

    @Override
    public void perform(Game game) {
        game.getActivePlayer().reloadWeapon(mWeaponIndex);
    }

    @Override
    public boolean isValid(Game game) {
        AmmoValue playerAmmo = game.getActivePlayer().getAmmo();
        Weapon weaponToReload = game.getActivePlayer().getWeapon(mWeaponIndex);
        return !weaponToReload.isLoaded() && playerAmmo.isBiggerOrEqual(weaponToReload.getReloadCost());
    }
}
