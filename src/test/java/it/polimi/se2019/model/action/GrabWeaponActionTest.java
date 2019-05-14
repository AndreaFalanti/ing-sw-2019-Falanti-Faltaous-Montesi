package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class GrabWeaponActionTest {

    @Test
    public void perform() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabWeaponAction action1 = new GrabWeaponAction(1);
        GrabWeaponAction action2 = new GrabWeaponAction(2);
        GrabWeaponAction action3 = new GrabWeaponAction(0);

        GrabWeaponAction action4 = new GrabWeaponAction(1, 2);

        Player player = game.getActivePlayer();
        AmmoValue fullAmmo = new AmmoValue(3,3,3);

        // go to blue spawn position
        player.move(new Position(2, 0));
        SpawnTile spawnTile = (SpawnTile) game.getBoard().getTileAt(game.getActivePlayer().getPos());
        Weapon[] expectedResult = {
                spawnTile.getWeapon(1),
                spawnTile.getWeapon(2),
                spawnTile.getWeapon(0)
        };
        AmmoValue[] expectedCosts = {
                expectedResult[0].getGrabCost(),
                expectedResult[1].getGrabCost(),
                expectedResult[2].getGrabCost()
        };

        // every time a grab is performed, refill player ammo to avoid invalid costs
        player.getAmmo().add(fullAmmo);
        action1.perform(game);
        assertEquals(fullAmmo.deepCopy().subtract(expectedCosts[0]), player.getAmmo());

        player.getAmmo().add(fullAmmo);
        action2.perform(game);
        assertEquals(fullAmmo.deepCopy().subtract(expectedCosts[1]), player.getAmmo());

        player.getAmmo().add(fullAmmo);
        action3.perform(game);
        assertEquals(fullAmmo.deepCopy().subtract(expectedCosts[2]), player.getAmmo());

        assertArrayEquals(expectedResult, game.getActivePlayer().getWeapons());
        // Spawn should be refilled after every grab (Weapon deck in this case has still 12 cards in theory)
        for (Weapon weapon : spawnTile.getWeapons()) {
            assertNotNull(weapon);
        }

        // go to red spawn position
        player.move(new Position(0, 1));
        SpawnTile spawnTile2 = (SpawnTile) game.getBoard().getTileAt(game.getActivePlayer().getPos());
        Weapon[] expectedResult2 = {
                expectedResult[0],
                expectedResult[1],
                spawnTile2.getWeapon(1) };
        Weapon exchangedWeapon = player.getWeapon(action4.getWeaponToExchangeIndex());

        player.getAmmo().add(fullAmmo);
        action4.perform(game);
        assertEquals(fullAmmo.deepCopy().subtract(expectedResult2[2].getGrabCost()), player.getAmmo());

        assertArrayEquals(expectedResult2, game.getActivePlayer().getWeapons());
        assertEquals(exchangedWeapon, spawnTile2.getWeapon(action4.getWeaponGrabbedIndex()));
    }

    @Test
    public void isValid() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        GrabWeaponAction action1 = new GrabWeaponAction(1);
        GrabWeaponAction action2 = new GrabWeaponAction(2, 1);

        // (0, 0) is not a spawn position
        assertTrue(action1.getErrorResponse(game).isPresent());

        // go to blue spawn position
        game.getActivePlayer().move(new Position(2, 0));
        assertFalse(action1.getErrorResponse(game).isPresent());

        assertTrue(action2.getErrorResponse(game).isPresent());

        // add weapons to player for making exchange possible
        game.getActivePlayer().addWeapon(new Weapon());
        game.getActivePlayer().addWeapon(new Weapon());
        game.getActivePlayer().addWeapon(new Weapon());

        assertFalse(action2.getErrorResponse(game).isPresent());
    }
}