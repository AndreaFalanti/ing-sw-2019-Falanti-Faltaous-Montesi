package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.WeaponIndexStrategy;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.action.response.*;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.Tile;

import java.util.Optional;

public class GrabWeaponAction implements GrabAction, CostlyAction {
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

    @Override
    public boolean[] getDiscardedCards() {
        return mDiscardedCards;
    }

    @Override
    public void setDiscardedCards(boolean[] discardedCards) {
        mDiscardedCards = discardedCards;
    }

    public void setWeaponToExchangeIndex(Integer weaponToExchangeIndex) {
        mWeaponToExchangeIndex = weaponToExchangeIndex;
    }

    @Override
    public void perform(Game game) {
        SpawnTile spawnTile = (SpawnTile) game.getBoard().getTileAt(game.getActivePlayer().getPos());
        Weapon grabbedWeapon = spawnTile.grabWeapon(mWeaponGrabbedIndex);
        Player player = game.getActivePlayer();

        grabbedWeapon.setLoaded(true);

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
    public Optional<InvalidActionResponse> getErrorResponse(Game game) {
        return getErrorMessageAtPos(game, game.getActivePlayer().getPos());
    }

    @Override
    public boolean consumeAction() {
        return true;
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public boolean leadToAShootInteraction() {
        return false;
    }

    @Override
    public Optional<InvalidActionResponse> getErrorMessageAtPos(Game game, Position pos) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return Optional.of(new MessageActionResponse(ActionResponseStrings.NO_ACTIONS_REMAINING));
        }

        Tile tile = game.getBoard().getTileAt(pos);
        if (tile.getTileType().equals("spawn")) {
            SpawnTile spawnTile = (SpawnTile) tile;
            Weapon weapon = spawnTile.getWeapon(mWeaponGrabbedIndex);
            Player player = game.getActivePlayer();

            if (weapon == null) {
                return Optional.of(new MessageActionResponse("Selected weapon to grab is null"));
            }

            if (!AmmoPayment.isValid(player, weapon.getGrabCost(), mDiscardedCards)) {
                AmmoValue remainingCost = weapon.getGrabCost().subtract(player.getAmmo(), true);
                return AmmoPayment.canPayWithPowerUps(player, remainingCost) ?
                        Optional.of(new DiscardRequiredActionResponse(ActionResponseStrings.DISCARD_MESSAGE, this)) :
                        Optional.of(new MessageActionResponse(ActionResponseStrings.NOT_ENOUGH_AMMO));
            }

            // player is grabbing a weapon but it has space in hand
            if (mWeaponToExchangeIndex == null) {
                return !player.isFullOfWeapons() ?
                        Optional.empty() :
                        Optional.of(
                                new SelectWeaponRequiredActionResponse("Your hand is full, select a weapon" +
                                        " to exchange", null, WeaponIndexStrategy.exchangeWeapon(), this)
                        );
            }
            // player is trying to exchange one of his weapon with spawn's one
            else {
                return player.isFullOfWeapons() ?
                        Optional.empty() :
                        Optional.of(new MessageActionResponse("Can't exchange weapon if your hand is not full"));
            }
        }

        // tile isn't a SpawnTile
        return Optional.of(new MessageActionResponse("Can't grab a weapon from a normal tile"));
    }
}
