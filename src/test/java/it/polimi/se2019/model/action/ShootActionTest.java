package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShootActionTest {

    @Test
    public void testPerform() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        Weapon weapon = new Weapon("a", new AmmoValue(), new AmmoValue());
        weapon.setLoaded(true);
        game.getActivePlayer().addWeapon(weapon);

        ShootAction action = new ShootAction(0);
        action.perform(game);
        assertFalse(weapon.isLoaded());
    }

    @Test
    public void testIsValid() {
        Game game = GameTestCaseBuilder.generateBaseGame();

        ShootAction action = new ShootAction(0);
        // active player has no weapon
        assertTrue(action.getErrorResponse(game).isPresent());

        Weapon weapon = new Weapon("a", new AmmoValue(), new AmmoValue());
        game.getActivePlayer().addWeapon(weapon);
        // weapon is not loaded
        assertTrue(action.getErrorResponse(game).isPresent());

        weapon.setLoaded(true);
        assertFalse(action.getErrorResponse(game).isPresent());
    }
}