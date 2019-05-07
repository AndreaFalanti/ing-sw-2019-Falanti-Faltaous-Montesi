package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class GrabAmmoActionTest {

    @Test
    public void perform() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabAmmoAction action = new GrabAmmoAction();
        NormalTile tile = (NormalTile)game.getBoard().getTileAt(game.getActivePlayer().getPos());
        AmmoValue addedAmmo = tile.getAmmoCard().getAmmoGain();
        AmmoValue expectedPlayerAmmo = game.getActivePlayer().getAmmo().deepCopy();

        expectedPlayerAmmo.add(addedAmmo);
        action.perform(game);
        assertEquals(expectedPlayerAmmo , game.getActivePlayer().getAmmo());
    }

    @Test
    public void isValid() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabAmmoAction action = new GrabAmmoAction();

        assertTrue(action.isValid(game));

        // spawn tile
        game.getActivePlayer().move(new Position(0, 2));
        assertFalse(action.isValid(game));
    }
}