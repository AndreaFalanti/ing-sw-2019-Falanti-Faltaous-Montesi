package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveActionTest {

    @Test
    public void testPerform() {
    }

    @Test
    public void testIsValid() {
        Game game1 = GameTestCaseBuilder.generateBaseGame();
        Game game2 = GameTestCaseBuilder.generateFinalFrenzyGame();
        game1.getActivePlayer().move(new Position(1,0));


        MoveAction action1 = new MoveAction(game1.getActivePlayer().getColor(), new Position(0, 0));
        MoveAction action2 = new MoveAction(game1.getActivePlayer().getColor(), new Position(3, 2));
        assertTrue(action1.isValid(game1));
        assertTrue(action1.isValid(game2));
        assertFalse(action2.isValid(game1));
        assertFalse(action2.isValid(game2));
    }
}