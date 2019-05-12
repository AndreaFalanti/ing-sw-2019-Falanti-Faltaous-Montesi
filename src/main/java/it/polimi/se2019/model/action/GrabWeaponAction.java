package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.responses.*;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.Tile;
import it.polimi.se2019.model.weapon.Weapon;

public class GrabWeaponAction implements GrabAction {
    private int mWeaponGrabbedIndex;
    private Integer mWeaponToExchangeIndex;
    private boolean[] mDiscardedCards = {false, false, false};

    public GrabWeaponAction (int index) {
        if (!isValidIndex(index)) {
            throw new IllegalArgumentException("Invalid weapon to grab index in weapon grab");
        }

        mWeaponGrabbedIndex = index;
        mWeaponToExchangeIndex = null;
    }

    public GrabWeaponAction (int index, boolean[] discardedCards) {
        this (index);

        if (discardedCards.length != 3) {
            throw new IllegalArgumentException("Illegal boolean array");
        }

        mDiscardedCards = discardedCards;
    }

    public GrabWeaponAction (int index, Integer weaponToExchangeIndex) {
        this(index);

        if (weaponToExchangeIndex != null && !isValidIndex(weaponToExchangeIndex)) {
            throw new IllegalArgumentException("Invalid weapon to exchange index in weapon grab");
        }
        mWeaponToExchangeIndex = weaponToExchangeIndex;
    }

    public GrabWeaponAction (int index, Integer weaponToExchangeIndex, boolean[] discardedCards) {
        this (index, weaponToExchangeIndex);

        if (discardedCards.length != 3) {
            throw new IllegalArgumentException("Illegal boolean array");
        }

        mDiscardedCards = discardedCards;
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

    public boolean[] getDiscardedCards() {
        return mDiscardedCards;
    }

    @Override
    public void perform(Game game) {
        SpawnTile spawnTile = (SpawnTile) game.getBoard().getTileAt(game.getActivePlayer().getPos());
        Weapon grabbedWeapon = spawnTile.grabWeapon(mWeaponGrabbedIndex);
        Player player = game.getActivePlayer();

        // if can't add weapon because hand is full, perform an exchange (catch block)
        if (mWeaponToExchangeIndex == null) {
            player.addWeapon(grabbedWeapon);
            spawnTile.addWeapon(game.getWeaponDeck().drawCard());
        }
        else {
            spawnTile.addWeapon(player.takeWeapon(mWeaponToExchangeIndex));
            player.addWeapon(grabbedWeapon);
        }

        AmmoPayment.payCost(player, grabbedWeapon.getGrabCost(), mDiscardedCards);
    }

    @Override
    public InvalidActionResponse getErrorResponse(Game game) {
        return getErrorMessageAtPos(game, game.getActivePlayer().getPos());
    }

    @Override
    public boolean consumeAction() {
        return true;
    }

    @Override
    public InvalidActionResponse getErrorMessageAtPos(Game game, Position pos) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING);
        }

        Tile tile = game.getBoard().getTileAt(pos);
        if (tile.getTileType().equals("spawn")) {
            SpawnTile spawnTile = (SpawnTile) tile;
            Weapon weapon = spawnTile.getWeapon(mWeaponGrabbedIndex);
            Player player = game.getActivePlayer();

            if (weapon == null) {
                return new MessageActionResponse("Selected weapon to grab is null");
            }

            if (!AmmoPayment.isValid(player, weapon.getGrabCost(), mDiscardedCards)) {
                return new DiscardRequiredActionResponse(ActionResponseStrings.DISCARD_MESSAGE);
            }

            // player is grabbing a weapon but it has space in hand
            if (mWeaponToExchangeIndex == null) {
                return !player.isFullOfWeapons() ?
                        null : new SelectWeaponRequiredActionResponse("Your hand is full, select a weapon to exchange");
            }
            // player is trying to exchange one of his weapon with spawn's one
            else {
                return player.isFullOfWeapons() ?
                        null : new MessageActionResponse("Can't exchange weapon if your hand is full");
            }
        }

        // tile isn't a SpawnTile
        return new MessageActionResponse("Can't grab a weapon from a normal tile");
    }
}
