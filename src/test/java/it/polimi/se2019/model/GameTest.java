package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    /**
     * Used to fill a list with valid players. [ORDER: blue, yellow, grey]
     * @param list List to fill
     */
    private void fillPlayerList (ArrayList<Player> list) {
        Player p1 = new Player("a", PlayerColor.BLUE);
        Player p2 = new Player("b", PlayerColor.YELLOW);
        Player p3 = new Player("c", PlayerColor.GREY);

        list.add(p1);
        list.add(p2);
        list.add(p3);
    }

    /**
     * Check that IllegalArgumentException is thrown for every case possible
     */
    @Test
    public void testGameConstructorIllegalArgumentException () {
        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        ArrayList<Player> players2 = new ArrayList<>();
        fillPlayerList(players2);
        players2.remove(0);

        Board board = new Board();

        try {
            Game game = new Game(null, players, 3);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            Game game = new Game(board, players2, 3);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        try {
            Game game = new Game(board, players, 0);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        // add a player with same color of an already existent one
        players.add(new Player("d", PlayerColor.BLUE));
        try {
            Game game = new Game(board, players, 3);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test all fundamental game interactions, for example dealing damage, scoring kills and turn finish.
     * After kills set in game are reached, isGameOver is set true only when all players have done
     * their last turn in final frenzy mode. This also mean testing all mechanics related to game over and
     * final frenzy, like total kills score distribution and number of actions available in final frenzy.
     */
    @Test
    public void testCompleteGameSession() {
        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        Game game = new Game(new Board(), players, 4);

        // Blue player, no kills
        game.startNextTurn();
        assertEquals(2, game.getRemainingActions());
        game.onTurnEnd();

        game.startNextTurn();
        // Yellow player, one kill [+8 points + 1 for first blood]
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.GREY,
                new Damage(11, 0));
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.BLUE,
                new Damage(6, 0));
        game.onTurnEnd();
        game.getPlayerFromColor(PlayerColor.GREY).respawn(null);
        assertEquals(9, game.getPlayerFromColor(PlayerColor.YELLOW).getScore());

        // Grey player, double kill (one overkill) [+14 points + 1 first blood + 1 for double kill]
        // Yellow player, contribution on blue kill [+8 points + 1 first blood]
        game.startNextTurn();
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.BLUE,
                new Damage(6, 0));
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.YELLOW,
                new Damage(11, 0));
        game.onTurnEnd();
        game.getPlayerFromColor(PlayerColor.BLUE).respawn(null);
        game.getPlayerFromColor(PlayerColor.YELLOW).respawn(null);
        assertEquals(16, game.getPlayerFromColor(PlayerColor.GREY).getScore());
        assertEquals(18, game.getPlayerFromColor(PlayerColor.YELLOW).getScore());
        // check that player has a mark of player overkilled after scoring overkill
        assertEquals(1, game.getPlayerFromColor(PlayerColor.GREY).getMarks().get(PlayerColor.BLUE).intValue());

        // Blue player, no kills
        game.startNextTurn();
        game.onTurnEnd();

        // Yellow player, one kill [+6+1 points, blue was killed one time]
        game.startNextTurn();
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.BLUE,
                new Damage(11, 0));
        game.onTurnEnd();
        game.getPlayerFromColor(PlayerColor.BLUE).respawn(null);
        assertEquals(25, game.getPlayerFromColor(PlayerColor.YELLOW).getScore());

        assertFalse(game.isGameOver());

        //start turn for every player and conclude final frenzy
        //turn order (player index): 2 -> 0 -> 1 -> gameOver
        for (int i = 0; i < players.size(); i++) {
            game.startNextTurn();

            //play before first player, has 2 actions
            if (game.getActivePlayerIndex() == 2) {
                assertEquals(2, game.getRemainingActions());
            }
            //play as or after first player, has only one action in final frenzy
            else {
                assertEquals(1, game.getRemainingActions());
            }

            game.onTurnEnd();
            assertFalse(game.isGameOver());
        }

        game.startNextTurn();
        assertTrue(game.isGameOver());

        // 2 yellow and 3 grey on killtrack (3 grey because of 2 kill and 1 bonus for overkill)
        // [+8 for grey, +6 for yellow]
        assertEquals(31, game.getPlayerFromColor(PlayerColor.YELLOW).getScore());
        assertEquals(24, game.getPlayerFromColor(PlayerColor.GREY).getScore());
        assertEquals(0, game.getPlayerFromColor(PlayerColor.BLUE).getScore());
    }

    /**
     * Test that deaths list is correct and that last kill correctly start final frenzy.
     */
    @Test
    public void testAddDeath() {
        ArrayList<PlayerColor> expectedResult = new ArrayList<> ();
        expectedResult.add(PlayerColor.BLUE);
        expectedResult.add(PlayerColor.YELLOW);

        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        Game game = new Game(new Board(), players, 2);

        // Blue's turn
        game.startNextTurn();
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.YELLOW,
                new Damage(11, 0));
        game.onTurnEnd();
        game.getPlayerFromColor(PlayerColor.YELLOW).respawn(null);

        // Yellow's turn
        game.startNextTurn();
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.BLUE,
                new Damage(11, 0));
        game.onTurnEnd();
        game.getPlayerFromColor(PlayerColor.BLUE).respawn(null);

        assertEquals(expectedResult, game.getKills());
        assertTrue(game.isFinalFrenzy());
        assertEquals(2, game.getFinalFrenzyTurnStart().intValue());
    }

    /**
     * Test that correct player is returned and that, if color is not present, the method
     * throw an exception.
     */
    @Test
    public void testGetPlayerFromColor() {
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

    /**
     * Test that score is distributed correctly when a player is killed
     */
    @Test
    public void testDistributePlayerKillScore() {
        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        Game game = new Game(new Board(), players, 4);

        game.startNextTurn();
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.GREY, new Damage(5, 0));
        game.onTurnEnd();

        game.startNextTurn();
        game.handleDamageInteraction(game.getActivePlayer().getColor(), PlayerColor.GREY, new Damage(7, 0));
        game.onTurnEnd();
        game.getPlayerFromColor(PlayerColor.GREY).respawn(null);

        assertEquals(1, game.getKills().size());
        assertEquals(PlayerColor.YELLOW, game.getKills().get(0));
        assertEquals(7, game.getPlayerFromColor(PlayerColor.BLUE).getScore());
        assertEquals(8, game.getPlayerFromColor(PlayerColor.YELLOW).getScore());
        assertEquals(0, game.getPlayerFromColor(PlayerColor.GREY).getScore());
    }

    /**
     * Test that leaderboard returned is correct
     */
    @Test
    public void getLeaderboard() {
        ArrayList<Player> players = new ArrayList<>();
        fillPlayerList(players);

        List<Player> expectedLeaderboard = new ArrayList<>();
        expectedLeaderboard.add(players.get(1));
        expectedLeaderboard.add(players.get(2));
        expectedLeaderboard.add(players.get(0));

        Game game = new Game(new Board(), players, 3);
        game.getPlayerFromColor(PlayerColor.BLUE).addScore(8);
        game.getPlayerFromColor(PlayerColor.YELLOW).addScore(12);
        game.getPlayerFromColor(PlayerColor.GREY).addScore(10);

        assertEquals(expectedLeaderboard, game.getLeaderboard());
        assertEquals(players.get(1), game.getWinner());
    }
}