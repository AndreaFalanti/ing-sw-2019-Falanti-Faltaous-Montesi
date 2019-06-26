package it.polimi.se2019.controller;

import it.polimi.se2019.controller.weapon.Weapons;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.action.GrabAmmoAction;
import it.polimi.se2019.model.action.MoveGrabAction;
import it.polimi.se2019.model.action.ReloadAction;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.util.GameTestCaseBuilder;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.ActionRequest;
import it.polimi.se2019.view.request.PowerUpDiscardedRequest;
import it.polimi.se2019.view.request.WeaponSelectedRequest;
import org.junit.Test;

import java.util.EnumMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ControllerTest {
    @Test
    public void testGrabActionStrategyCompletion () {
        Game game = GameTestCaseBuilder.generateBaseGame();
        View viewMock = mock(View.class);
        EnumMap<PlayerColor, View> mViewMap = new EnumMap<>(PlayerColor.class);

        mViewMap.put(PlayerColor.BLUE, viewMock);
        Controller controller = new Controller(game, mViewMap);
        Player activePlayer = game.getActivePlayer();

        // move to spawn
        activePlayer.move(new Position(2, 0));
        // fill ammo
        activePlayer.addAmmo(new AmmoValue(3,3,3));
        controller.handle(new ActionRequest(new GrabAmmoAction(), PlayerColor.BLUE));

        controller.handle(new WeaponSelectedRequest(1, PlayerColor.BLUE));
        assertNotNull(activePlayer.getWeapon(0));

        System.out.println(activePlayer.getWeapon(0));

        // move out of spawn
        activePlayer.move(new Position(1, 0));
        controller.handle(new ActionRequest(new MoveGrabAction(PlayerColor.BLUE, new Position(2, 0)), PlayerColor.BLUE));

        controller.handle(new WeaponSelectedRequest(1, PlayerColor.BLUE));
        assertNotNull(activePlayer.getWeapon(1));

        System.out.println(activePlayer.getWeapon(1));
    }

    @Test
    public void testCostlyActionCompletion () {
        Game game = GameTestCaseBuilder.generateBaseGame();
        View viewMock = mock(View.class);
        EnumMap<PlayerColor, View> mViewMap = new EnumMap<>(PlayerColor.class);

        mViewMap.put(PlayerColor.BLUE, viewMock);
        Controller controller = new Controller(game, mViewMap);
        Player activePlayer = game.getActivePlayer();

        // move to spawn
        activePlayer.move(new Position(1, 0));

        SpawnTile spawnTile = (SpawnTile)game.getBoard().getTileAt(new Position(2, 0));
        // set first weapon to null
        spawnTile.grabWeapon(0);
        // add machineGun to index 0
        spawnTile.addWeapon(Weapons.get("machine_gun"));

        // add powerUp with red ammo and yellow ammo
        activePlayer.addPowerUp(new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(1, 0, 0)));
        activePlayer.addPowerUp(new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0, 1, 0)));
        assertNotNull(activePlayer.getPowerUpCard(0));
        assertNotNull(activePlayer.getPowerUpCard(1));

        activePlayer.addWeapon(Weapons.get("sledgehammer"));
        assertFalse(activePlayer.getWeapon(0).isLoaded());

        // reset all ammo to 0
        activePlayer.payAmmo(new AmmoValue(1, 1, 1));
        assertEquals(new AmmoValue(0, 0, 0), activePlayer.getAmmo());


        controller.handle(new ActionRequest(new MoveGrabAction(PlayerColor.BLUE, new Position(2, 0)), PlayerColor.BLUE));

        controller.handle(new WeaponSelectedRequest(0, PlayerColor.BLUE));
        controller.handle(new PowerUpDiscardedRequest(new boolean[] {true, false, false}, PlayerColor.BLUE));
        assertNull(activePlayer.getPowerUpCard(0));
        assertEquals(new AmmoValue(0, 0, 0), activePlayer.getAmmo());
        assertNotNull(activePlayer.getWeapon(1));

        System.out.println(activePlayer.getWeapon(1));

        // reload can only be performed at turn's end
        game.decreaseActionCounter();
        assertEquals(0, game.getRemainingActions());

        controller.handle(new ActionRequest(new ReloadAction(0), PlayerColor.BLUE));

        controller.handle(new PowerUpDiscardedRequest(new boolean[] {false, true, false}, PlayerColor.BLUE));
        assertTrue(activePlayer.getWeapon(0).isLoaded());
        assertNull(activePlayer.getPowerUpCard(0));
        assertNull(activePlayer.getPowerUpCard(1));
        assertEquals(new AmmoValue(0, 0, 0), activePlayer.getAmmo());

        //System.out.println(activePlayer.getWeapon(0));
    }
}