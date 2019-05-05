package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.FullHandException;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PowerUpCard;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.model.board.Tile;

public class GrabAmmoAction implements GrabAction {
    @Override
    public void perform(Game game) {
        NormalTile tile = (NormalTile)game.getBoard().getTileAt(game.getActivePlayer().getPos());
        AmmoCard ammoCard = tile.grabAmmo();
        game.getActivePlayer().getAmmo().add(ammoCard.getAmmoGain());

        if (ammoCard.getDrawPowerUp()) {
            PowerUpCard powerUpCard = game.getPowerUpDeck().drawCard();
            try {
                game.getActivePlayer().addPowerUp(powerUpCard);
            } catch (FullHandException e) {
                System.out.println("Hand is full, can't draw power up card");
            }
        }
    }

    @Override
    public boolean isValid(Game game) {
        Tile tile = game.getBoard().getTileAt(game.getActivePlayer().getPos());
        if (tile.getTileType().equals("normal")) {
            NormalTile normalTile = (NormalTile) tile;
            return normalTile.getAmmoCard() != null;
        }
        return false;
    }
}
