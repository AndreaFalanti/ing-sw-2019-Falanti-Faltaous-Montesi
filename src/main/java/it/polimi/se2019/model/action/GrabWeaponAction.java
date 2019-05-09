package it.polimi.se2019.model.action;

import it.polimi.se2019.model.FullHandException;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.Tile;
import it.polimi.se2019.model.weapon.Weapon;

public class GrabWeaponAction implements GrabAction {
    private int mWeaponGrabbedIndex;
    private Integer mWeaponToExchangeIndex;

    public GrabWeaponAction (int index) {
        if (!isValidIndex(index)) {
            throw new IllegalArgumentException("Invalid weapon to grab index in weapon grab");
        }

        mWeaponGrabbedIndex = index;
        mWeaponToExchangeIndex = null;
    }

    public GrabWeaponAction (int index, Integer weaponToExchangeIndex) {
        this(index);

        if (weaponToExchangeIndex != null && !isValidIndex(weaponToExchangeIndex)) {
            throw new IllegalArgumentException("Invalid weapon to exchange index in weapon grab");
        }
        mWeaponToExchangeIndex = weaponToExchangeIndex;
    }

    private boolean isValidIndex (int index) {
        return index >= 0 && index < 3;
    }

    public int getWeaponGrabbedIndex() {
        return mWeaponGrabbedIndex;
    }

    public Integer getWeaponToExchangeIndex() {
        return mWeaponToExchangeIndex;
    }

    //TODO: in perform and isValid add check of weapon grab cost and relative ammo payment

    @Override
    public void perform(Game game) {
        SpawnTile spawnTile = (SpawnTile) game.getBoard().getTileAt(game.getActivePlayer().getPos());
        Weapon grabbedWeapon = spawnTile.grabWeapon(mWeaponGrabbedIndex);

        // TODO: could be refactored, it should'nt use exceptions to decide action behaviour
        // if can't add weapon because hand is full, perform an exchange (catch block)
        try {
            game.getActivePlayer().addWeapon(grabbedWeapon);
            spawnTile.addWeapon(game.getWeaponDeck().drawCard());
        }
        catch (FullHandException e) {
            Player player = game.getActivePlayer();
            spawnTile.addWeapon(player.takeWeapon(mWeaponToExchangeIndex));

            try {
                player.addWeapon(grabbedWeapon);
            }
            // now it shouldn't throw exception because it gave a weapon to spawn tile
            catch (FullHandException e1) {
                // CRASH THE APP!!!
                e1.printStackTrace();
            }
        }
    }

    @Override
    public boolean isValid(Game game) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return false;
        }

        Tile tile = game.getBoard().getTileAt(game.getActivePlayer().getPos());
        if (tile.getTileType().equals("spawn")) {
            SpawnTile spawnTile = (SpawnTile) tile;

            // player is grabbing a weapon but it has space in hand
            if (mWeaponToExchangeIndex == null) {
                return spawnTile.getWeapon(mWeaponGrabbedIndex) != null
                        && !game.getActivePlayer().isFullOfWeapons();
            }
            // player is trying to exchange one of his weapon with spawn's one
            else {
                Player player = game.getActivePlayer();
                return spawnTile.getWeapon(mWeaponGrabbedIndex) != null
                        && player.getWeapon(mWeaponToExchangeIndex) != null
                        && player.isFullOfWeapons();
            }
        }

        // tile isn't a SpawnTile
        return false;
    }
}
