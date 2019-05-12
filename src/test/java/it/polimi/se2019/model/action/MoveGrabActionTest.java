package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveGrabActionTest {

    @Test
    public void perform() {
        Game game1 = GameTestCaseBuilder.generateBaseGame();
        Game game2 = GameTestCaseBuilder.generateFinalFrenzyGameAfterFirstPlayer();

        MoveGrabAction action1 = new MoveGrabAction(game1.getActivePlayer().getColor(), new Position(1, 0));
        MoveGrabAction action2 =
                new MoveGrabAction(game1.getActivePlayer().getColor(), new Position(2, 0), 0);

        MoveGrabAction action3 = new MoveGrabAction(game2.getActivePlayer().getColor(), new Position(1, 2));

        NormalTile tile1 = (NormalTile) game1.getBoard().getTileAt(action1.getMoveAction().getDestination());
        tile1.setAmmoCard(new AmmoCard(new AmmoValue(0,0,2), true));

        SpawnTile tile2 = (SpawnTile) game1.getBoard().getTileAt(action2.getMoveAction().getDestination());

        NormalTile tile3 = (NormalTile) game2.getBoard().getTileAt(action3.getMoveAction().getDestination());
        tile3.setAmmoCard(new AmmoCard(new AmmoValue(1,0,2), false));

        Weapon weapon = tile2.getWeapon(0);

        action1.perform(game1);
        // check that player gained ammo and a powerUp card
        assertEquals(new AmmoValue(1,1,3), game1.getActivePlayer().getAmmo());
        assertNotNull(game1.getActivePlayer().getPowerUpCard(0));

        action2.perform(game1);
        assertEquals(new AmmoValue(1,1,3).subtract(weapon.getGrabCost()), game1.getActivePlayer().getAmmo());

        action3.perform(game2);
        // check that player gain ammo but not powerUp card
        assertEquals(new AmmoValue(2,1,3), game2.getActivePlayer().getAmmo());
        assertNull(game2.getActivePlayer().getPowerUpCard(0));
    }

    @Test
    public void isValid() {
        Game game1 = GameTestCaseBuilder.generateBaseGame();
        Game game2 = GameTestCaseBuilder.generateFinalFrenzyGameBeforeFirstPlayer();
        Game game3 = GameTestCaseBuilder.generateFinalFrenzyGameAfterFirstPlayer();

        MoveGrabAction action1 = new MoveGrabAction(game1.getActivePlayer().getColor(), new Position(1, 0));
        MoveGrabAction action2 = new MoveGrabAction(game1.getActivePlayer().getColor(), new Position(0, 1));
        MoveGrabAction action3 =
                new MoveGrabAction(game1.getActivePlayer().getColor(), new Position(0, 1), 2);
        MoveGrabAction action4 =
                new MoveGrabAction(game1.getActivePlayer().getColor(), new Position(1, 0), 1);
        MoveGrabAction action5 = new MoveGrabAction(game1.getActivePlayer().getColor(), new Position(1, 1));

        MoveGrabAction action6 = new MoveGrabAction(game2.getActivePlayer().getColor(), new Position(1, 1));
        MoveGrabAction action7 = new MoveGrabAction(game2.getActivePlayer().getColor(), new Position(1, 2));

        MoveGrabAction action8 = new MoveGrabAction(game3.getActivePlayer().getColor(), new Position(1, 2));
        MoveGrabAction action9 = new MoveGrabAction(PlayerColor.PURPLE, new Position(1, 2));


        // try to grab ammo at distance 1 from player
        assertNull(action1.getErrorResponse(game1));

        // try to grab ammo from a spawn tile
        assertNotNull(action2.getErrorResponse(game1));

        // try to grab a weapon from spawn tile at distance 1
        assertNull(action3.getErrorResponse(game1));

        // try to grab a weapon from normal tile
        assertNotNull(action4.getErrorResponse(game1));

        // try to grab ammo at distance 2
        assertNotNull(action5.getErrorResponse(game1));

        // try to grab ammo at distance 2 in final frenzy status (before first player)
        assertNull(action6.getErrorResponse(game2));

        // try to grab ammo at distance 3 in final frenzy status (before first player)
        assertNotNull(action7.getErrorResponse(game2));

        // try to grab ammo at distance 3 in final frenzy status (after first player)
        assertNull(action8.getErrorResponse(game3));

        // try to do action with an invalid color
        assertNotNull(action9.getErrorResponse(game3));
    }
}