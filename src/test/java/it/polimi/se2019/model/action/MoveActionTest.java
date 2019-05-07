package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveActionTest {

    @Test
    public void testPerform() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        MoveAction action1 = new MoveAction(game.getActivePlayer().getColor(), new Position(1, 0), true);
        MoveAction action2 = new MoveAction(game.getActivePlayer().getColor(), new Position(2, 1), true);

        action1.perform(game);
        assertEquals(new Position(1, 0), game.getActivePlayer().getPos());
        action2.perform(game);
        assertEquals(new Position(2, 1), game.getActivePlayer().getPos());
    }

    @Test
    public void testIsValid() {
        Game game1 = GameTestCaseBuilder.generateBaseGame();
        Game game2 = GameTestCaseBuilder.generateFinalFrenzyGameBeforeFirstPlayer();
        Game game3 = GameTestCaseBuilder.generateFinalFrenzyGameAfterFirstPlayer();

        System.out.println("Game 1 active player: " + game1.getActivePlayerIndex());
        System.out.println("Game 2 active player: " + game2.getActivePlayerIndex());
        System.out.println("Game 3 active player: " + game3.getActivePlayerIndex());

        MoveAction[] action1 = {
                new MoveAction(game1.getActivePlayer().getColor(), new Position(1, 0), true),
                new MoveAction(game2.getActivePlayer().getColor(), new Position(1, 0), true),
                new MoveAction(game3.getActivePlayer().getColor(), new Position(1, 0), true)
        };

        MoveAction[] action2 = {
                new MoveAction(game1.getActivePlayer().getColor(), new Position(3, 2), true),
                new MoveAction(game2.getActivePlayer().getColor(), new Position(3, 2), true),
                new MoveAction(game3.getActivePlayer().getColor(), new Position(3, 2), true)
        };

        MoveAction[] action3 = {
                new MoveAction(game1.getActivePlayer().getColor(), new Position(3, 1), true),
                new MoveAction(game2.getActivePlayer().getColor(), new Position(3, 1), true),
                new MoveAction(game3.getActivePlayer().getColor(), new Position(3, 1), true)
        };

        MoveAction[] action4 = {
                new MoveAction(game1.getActivePlayer().getColor(), new Position(0, 0), true),
                new MoveAction(game2.getActivePlayer().getColor(), new Position(0, 0), true),
                new MoveAction(game3.getActivePlayer().getColor(), new Position(0, 0), true)
        };

        MoveAction[] action5 = {
                new MoveAction(game1.getActivePlayer().getColor(), new Position(1, 0), false),
                new MoveAction(game2.getActivePlayer().getColor(), new Position(1, 0), false),
                new MoveAction(game3.getActivePlayer().getColor(), new Position(1, 0), false)
        };
        MoveAction[] action6 = {
                new MoveAction(game1.getActivePlayer().getColor(), new Position(3, 1), false),
                new MoveAction(game2.getActivePlayer().getColor(), new Position(3, 1), false),
                new MoveAction(game3.getActivePlayer().getColor(), new Position(3, 1), false)
        };

        //NOTE: all players are in pos(0, 0)
        // distance = 1
        assertTrue(action1[0].isValid(game1));
        assertTrue(action1[1].isValid(game2));
        assertFalse(action1[2].isValid(game3));

        // distance = 5
        assertFalse(action2[0].isValid(game1));
        assertFalse(action2[1].isValid(game2));
        assertFalse(action2[2].isValid(game3));

        // distance = 4
        assertFalse(action3[0].isValid(game1));
        assertTrue(action3[1].isValid(game2));
        assertFalse(action3[2].isValid(game3));

        // same position, distance = 0, always false
        assertFalse(action4[0].isValid(game1));
        assertFalse(action4[1].isValid(game2));
        assertFalse(action4[2].isValid(game3));

        assertTrue(action5[0].isValid(game1));
        assertTrue(action5[1].isValid(game2));
        assertTrue(action5[2].isValid(game3));

        assertFalse(action6[0].isValid(game1));
        assertFalse(action6[1].isValid(game2));
        assertFalse(action6[2].isValid(game3));
    }
}