package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GrabAmmoActionTest {

    @Test
    public void perform() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabAmmoAction action = new GrabAmmoAction();
        NormalTile tile = (NormalTile)game.getBoard().getTileAt(game.getActivePlayer().getPos());

        // set a card with a powerUp draw to test all the perform cases
        tile.setAmmoCard(new AmmoCard(new AmmoValue(0,1,1), true));

        AmmoValue addedAmmo = tile.getAmmoCard().getAmmoGain();
        AmmoValue expectedPlayerAmmo = game.getActivePlayer().getAmmo().deepCopy();
        List<PowerUpCard> deck = game.getPowerUpDeck().getDeck();
        PowerUpCard powerUpCard = deck.get(deck.size() - 1);
        expectedPlayerAmmo.add(addedAmmo);

        action.perform(game);
        assertEquals(expectedPlayerAmmo , game.getActivePlayer().getAmmo());
        // player had no power Up cards, it should have now it's first card equals to the one that has drawn from deck
        assertEquals(powerUpCard, game.getActivePlayer().getPowerUpCard(0));
    }

    @Test
    public void isValid() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabAmmoAction action = new GrabAmmoAction();

        assertNull(action.getErrorResponse(game));

        // spawn tile
        game.getActivePlayer().move(new Position(0, 2));
        assertNotNull(action.getErrorResponse(game));
    }
}