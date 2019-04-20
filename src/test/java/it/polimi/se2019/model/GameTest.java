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

        Game game = new Game(new Board(), players, 3);
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.YELLOW);

        //start turn for every player and conclude final frenzy
        for (int i = 0; i < players.size(); i++) {
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

        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        Game game = new Game(new Board(), players, 2);
        game.startNextTurn();
        game.addDeath(PlayerColor.YELLOW);
        game.addDeath(PlayerColor.BLUE);

        assertEquals(expectedResult, game.getDeaths());
        assertTrue(game.isFinalFrenzy());
        assertEquals(1, game.getFinalFrenzyTurnStart());
    }
}