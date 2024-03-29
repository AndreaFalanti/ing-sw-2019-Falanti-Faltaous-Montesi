package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class TeleportActionTest {
    @Test
    public void testPerform() {
        Game game = GameTestCaseBuilder.generateGameWithAllPowerUpsToPlayers();
        TeleportAction action1 = new TeleportAction(new Position(1,2), 0);

        game.startNextTurn();
        action1.perform(game);
        assertEquals(new Position(1,2), game.getActivePlayer().getPos());
        assertNull(game.getActivePlayer().getPowerUpCard(action1.getTeleportIndex()));
    }

    @Test
    public void testIsValid() {
        Game game = GameTestCaseBuilder.generateGameWithAllPowerUpsToPlayers();
        TeleportAction action1 = new TeleportAction(new Position(1,2), 0);
        TeleportAction action2 = new TeleportAction(new Position(2,3), 2);

        game.startNextTurn();
        assertFalse(action1.getErrorResponse(game).isPresent());
        assertTrue(action2.getErrorResponse(game).isPresent());
    }

    /**
     * Test that card is correctly discarded after use
     */
    @Test
    public void testNotValidAfterPerform() {
        Game game = GameTestCaseBuilder.generateGameWithAllPowerUpsToPlayers();
        TeleportAction action1 = new TeleportAction(new Position(1,2), 0);

        game.startNextTurn();
        assertFalse(action1.getErrorResponse(game).isPresent());
        action1.perform(game);
        assertTrue(action1.getErrorResponse(game).isPresent());
    }
}