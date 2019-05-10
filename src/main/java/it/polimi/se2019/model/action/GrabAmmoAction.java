package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.model.board.Tile;

public class GrabAmmoAction implements GrabAction {
    @Override
    public void perform(Game game) {
        NormalTile tile = (NormalTile)game.getBoard().getTileAt(game.getActivePlayer().getPos());
        AmmoCard ammoCard = tile.grabAmmo();
        Player player = game.getActivePlayer();

        player.getAmmo().add(ammoCard.getAmmoGain());

        // check if grabbed ammo card allows drawing a power up card
        if (ammoCard.getDrawPowerUp() && !player.isFullOfPowerUps()) {
            PowerUpCard powerUpCard = game.getPowerUpDeck().drawCard();
            try {
                player.addPowerUp(powerUpCard);
            } catch (FullHandException e) {
                System.out.println("Hand is full, can't draw power up card");
            }
        }
    }

    @Override
    public boolean isValid(Game game) {
        return isValidAtPos(game, game.getActivePlayer().getPos());
    }

    @Override
    public boolean isValidAtPos(Game game, Position pos) {
        // can't perform "costly" actions if they are no more available in this turn
        if (game.getRemainingActions() == 0) {
            return false;
        }

        // see if tile has still an ammo card or it was already picked
        Tile tile = game.getBoard().getTileAt(pos);
        if (tile != null && tile.getTileType().equals("normal")) {
            NormalTile normalTile = (NormalTile) tile;
            return normalTile.getAmmoCard() != null;
        }

        // tile isn't an AmmoTile
        return false;
    }
}
