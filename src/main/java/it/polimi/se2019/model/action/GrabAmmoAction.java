package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.responses.ActionResponseStrings;
import it.polimi.se2019.model.action.responses.InvalidActionResponse;
import it.polimi.se2019.model.action.responses.MessageActionResponse;
import it.polimi.se2019.model.action.responses.SelectWeaponRequiredActionResponse;
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
        if (ammoCard.getDrawPowerUp()) {
            if (!player.isFullOfPowerUps()) {
                PowerUpCard powerUpCard = game.getPowerUpDeck().drawCard();
                player.addPowerUp(powerUpCard);
            }
            else {
                System.out.println("Hand is full, can't draw power up card");
            }
        }
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

        // see if tile has still an ammo card or it was already picked
        Tile tile = game.getBoard().getTileAt(pos);
        if (tile != null && tile.getTileType().equals("normal")) {
            NormalTile normalTile = (NormalTile) tile;
            if(normalTile.getAmmoCard() == null){
                return new MessageActionResponse("Tile is empty, you can't grab here again");
            }
            else {
                return null;
            }
        }

        // tile isn't an AmmoTile
        return new SelectWeaponRequiredActionResponse("Select a weapon from spawn tile");
    }
}
