package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
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

        // TODO: fix test adding cost checks
        player.getAmmo().add(fullAmmo);
        action1.perform(game);
        //assertEquals(fullAmmo.subtract());
        action2.perform(game);
        action3.perform(game);

        assertArrayEquals(expectedResult, game.getActivePlayer().getWeapons());

        // go to red spawn position
        game.getActivePlayer().move(new Position(0, 1));
        SpawnTile spawnTile2 = (SpawnTile) game.getBoard().getTileAt(game.getActivePlayer().getPos());
        Weapon[] expectedResult2 = {
                expectedResult[0],
                expectedResult[1],
                spawnTile2.getWeapon(1) };

        action4.perform(game);

        assertArrayEquals(expectedResult2, game.getActivePlayer().getWeapons());
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

        assertFalse(action2.isValid(game));

        // add weapons to player for making exchange possible
        try {
            game.getActivePlayer().addWeapon(new Weapon());
            game.getActivePlayer().addWeapon(new Weapon());
            game.getActivePlayer().addWeapon(new Weapon());
        } catch (FullHandException e) {
            e.printStackTrace();
        }

        assertTrue(action2.isValid(game));
    }
}