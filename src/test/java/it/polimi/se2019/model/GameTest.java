package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {

    private void fillPlayerList (ArrayList<Player> list) {
        Player p1 = new Player("a", PlayerColor.BLUE);
        Player p2 = new Player("b", PlayerColor.YELLOW);
        Player p3 = new Player("c", PlayerColor.GREY);

        list.add(p1);
        list.add(p2);
        list.add(p3);
    }

    /**
     * Tests that, after kills set in game are reached, isGameOver is true only when all players have done
     * their last turn in final frenzy mode.
     */
    @Test
    public void testIsGameOver() {
        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        Game game = new Game(new Board(), players, 4);
        game.startNextTurn();
        game.addDeath(PlayerColor.GREY);
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.GREY);
        assertFalse(game.isGameOver());

        //start turn for every player and conclude final frenzy
        for (int i = 0; i < players.size(); i++) {
            game.startNextTurn();
            assertFalse(game.isGameOver());
        }
        game.startNextTurn();
        assertTrue(game.isGameOver());

        assertEquals(8, game.getPlayerFromColor(PlayerColor.YELLOW).getScore());
        assertEquals(6, game.getPlayerFromColor(PlayerColor.GREY).getScore());
        assertEquals(0, game.getPlayerFromColor(PlayerColor.BLUE).getScore());
    }

    /**
     * Test that deaths list is correct and that last kill correctly start final frenzy.
     */
    @Test
    public void addDeath() {
        ArrayList<PlayerColor> expectedResult = new ArrayList<> ();
        expectedResult.add(PlayerColor.YELLOW);
        expectedResult.add(PlayerColor.BLUE);

        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        Game game = new Game(new Board(), players, 2);
        game.startNextTurn();
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.BLUE);

        assertEquals(expectedResult, game.getKills());
        assertTrue(game.isFinalFrenzy());
        assertEquals(1, game.getFinalFrenzyTurnStart().intValue());
    }

    /**
     * Test that correct player is returned and that, if color is not present, the method
     * throw an exception.
     */
    @Test
    public void getPlayerFromColor() {
        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        Game game = new Game(new Board(), players, 5);
        try {
            Player p1 = game.getPlayerFromColor(PlayerColor.YELLOW);
            assertEquals(players.get(1), p1);
        }
        catch (IllegalArgumentException e) {
            fail();
        }

        try {
            Player p2 = game.getPlayerFromColor(PlayerColor.PURPLE);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }

    }
}