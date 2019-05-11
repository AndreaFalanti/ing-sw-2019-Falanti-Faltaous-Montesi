package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.model.board.Tile;

public class GrabAmmoAction implements GrabAction {
    private ResponseCode mCode;

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
                this.mCode = ResponseCode.OK;
            }
            else {
                System.out.println("Hand is full, can't draw power up card");
                this.mCode = ResponseCode.MAX_POWERUPS;
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
            System.out.println("Max number of action reached");
            this.mCode = ResponseCode.NO_ACTION_LEFT;
            return false;
        }

        // see if tile has still an ammo card or it was already picked
        Tile tile = game.getBoard().getTileAt(pos);
        if (tile != null && tile.getTileType().equals("normal")) {
            NormalTile normalTile = (NormalTile) tile;
            if(normalTile.getAmmoCard() == null){
                System.out.println("This card it is already picked");
                this.mCode = ResponseCode.ALREADY_PICKED;
                return false;
            }
            else {
                System.out.println("Card is picked");
                this.mCode = ResponseCode.OK;
                return true;
            }
        }

        // tile isn't an AmmoTile
        this.mCode = ResponseCode.NOT_AMMO_TILE;
        return false;
    }


    public ResponseCode getCode(){return mCode;}
}
