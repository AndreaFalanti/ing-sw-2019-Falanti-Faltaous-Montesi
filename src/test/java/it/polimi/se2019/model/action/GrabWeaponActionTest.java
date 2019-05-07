package it.polimi.se2019.model.action;

import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class GrabWeaponActionTest {

    @Test
    public void perform() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabWeaponAction action1 = new GrabWeaponAction(1);

        // go to blue spawn position
        game.getActivePlayer().move(new Position(2, 0));
    }

    @Test
    public void isValid() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabWeaponAction action1 = new GrabWeaponAction(1);
        GrabWeaponAction action2 = new GrabWeaponAction(2, 1);

        // (0, 0) is not a spawn position
        assertFalse(action1.isValid(game));

        // go to blue spawn position
        game.getActivePlayer().move(new Position(2, 0));
        assertTrue(action1.isValid(game));
    }
}