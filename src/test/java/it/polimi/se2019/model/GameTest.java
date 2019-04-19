package it.polimi.se2019.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {

    /**
     * Tests that, after kills set in game are reached, isGameOver is true only when all players have done
     * their last turn in final frenzy mode.
     */
    @Test
    public void testIsGameOver() {
        Player[] players = new Player[4];
        Game game = new Game(null, players, 3);
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.YELLOW);

        //start turn for every player and conclude final frenzy
        for (int i = 0; i < players.length; i++) {
            assertFalse(game.isGameOver());
            game.startNextTurn();
        }
        assertTrue(game.isGameOver());
    }

    /**
     * Test that deaths list is correct and that last kill correctly start final frenzy.
     */
    @Test
    public void addDeath() {
        ArrayList<PlayerColor> expectedResult = new ArrayList<> ();
        expectedResult.add(PlayerColor.YELLOW);
        expectedResult.add(PlayerColor.BLUE);

        Game game = new Game(null, null, 2);
        game.startNextTurn();
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.BLUE);

        assertEquals(expectedResult, game.getDeaths());
        assertEquals(1, game.getFinalFrenzyTurnStart());
    }
}