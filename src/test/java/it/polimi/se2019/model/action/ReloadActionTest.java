package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.FullHandException;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReloadActionTest {

    @Test
    public void testPerform() {
        Game game1 = GameTestCaseBuilder.generateBaseGame();
        Game game2 = GameTestCaseBuilder.generateGameWithAllPowerUpsToPlayers();

        boolean[] discardedCards = {true, false, true};
        ReloadAction action1 = new ReloadAction(0);
        ReloadAction action2 = new ReloadAction(1, discardedCards);

        AmmoValue expectedAmmo1 = new AmmoValue(0,0,1);
        AmmoValue expectedAmmo2 = new AmmoValue(0,1,0);

        try {
            game1.getActivePlayer()
                    .addWeapon(new Weapon("a", new AmmoValue(1,1,0), new AmmoValue(1,0,0)));
            game2.getActivePlayer()
                    .addWeapon(new Weapon("c", new AmmoValue(1,0,1), new AmmoValue(1,0,0)));
            game2.getActivePlayer()
                    .addWeapon(new Weapon("d", new AmmoValue(2,0,2), new AmmoValue(2,0,1)));
        }
        catch (FullHandException e) {
            e.printStackTrace();
        }

        action1.perform(game1);
        assertEquals(expectedAmmo1, game1.getActivePlayer().getAmmo());

        action2.perform(game2);
        assertEquals(expectedAmmo2, game2.getActivePlayer().getAmmo());
        assertNull(game2.getActivePlayer().getPowerUpCard(0));
        assertNull(game2.getActivePlayer().getPowerUpCard(2));
    }

    @Test
    public void testIsValid() {
        // Note: all players start with (1, 1, 1) ammo
        Game game1 = GameTestCaseBuilder.generateBaseGame();
        Game game2 = GameTestCaseBuilder.generateGameWithAllPowerUpsToPlayers();

        boolean[] discardedCards1 = {true, false, false};
        boolean[] discardedCards2 = {true, false, true};
        ReloadAction action1 = new ReloadAction(0);
        ReloadAction action2 = new ReloadAction(1);
        ReloadAction action3 = new ReloadAction(1, discardedCards1);
        ReloadAction action4 = new ReloadAction(1, discardedCards2);
        ReloadAction action5 = new ReloadAction(0, discardedCards1);
        ReloadAction action6 = new ReloadAction(2);

        try {
            game1.getActivePlayer()
                    .addWeapon(new Weapon("a", new AmmoValue(1,1,0), new AmmoValue(1,0,0)));
            game1.getActivePlayer()
                    .addWeapon(new Weapon("b", new AmmoValue(1,0,2), new AmmoValue(1,0,1)));
            game2.getActivePlayer()
                    .addWeapon(new Weapon("c", new AmmoValue(1,0,1), new AmmoValue(1,0,0)));
            game2.getActivePlayer()
                    .addWeapon(new Weapon("d", new AmmoValue(2,0,2), new AmmoValue(2,0,1)));
        }
        catch (FullHandException e) {
            e.printStackTrace();
        }

        // 2 action remaining, can't perform reload
        assertTrue(action1.getErrorResponse(game1).isPresent());

        // set to 0 the remaining actions
        game1.decreaseActionCounter();
        game1.decreaseActionCounter();
        game2.decreaseActionCounter();
        game2.decreaseActionCounter();

        // first weapon can be reloaded
        assertFalse(action1.getErrorResponse(game1).isPresent());
        assertFalse(action1.getErrorResponse(game2).isPresent());

        // first weapon can be reloaded without discarding, also check that if cards are null
        // is not throwing exception in game1
        assertTrue(action5.getErrorResponse(game1).isPresent());
        assertTrue(action5.getErrorResponse(game2).isPresent());

        // second weapon can't be reloaded without discarding (not enough ammo)
        assertTrue(action2.getErrorResponse(game1).isPresent());
        assertTrue(action2.getErrorResponse(game2).isPresent());

        // card discarded don't cover the remaining cost
        assertTrue(action3.getErrorResponse(game2).isPresent());

        // card discarded cover the remaining cost
        assertFalse(action4.getErrorResponse(game2).isPresent());

        // trying to reload a null weapon
        assertTrue(action6.getErrorResponse(game1).isPresent());

        // trying to reload an already loaded weapon
        game1.getActivePlayer().getWeapon(0).setLoaded(true);
        assertTrue(action1.getErrorResponse(game1).isPresent());
    }
}