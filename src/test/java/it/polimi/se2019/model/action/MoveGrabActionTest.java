package it.polimi.se2019.model.action;

import it.polimi.se2019.model.AmmoCard;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.NormalTile;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.weapon.Weapon;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        SpawnTile tile2 = (SpawnTile) game1.getBoard().getTileAt(action2.getMoveAction().getDestination());
        NormalTile tile3 = (NormalTile) game2.getBoard().getTileAt(action3.getMoveAction().getDestination());

        AmmoCard ammoCard1 = tile1.getAmmoCard();
        AmmoCard ammoCard2 = tile3.getAmmoCard();
        Weapon weapon = tile2.getWeapon(0);

        action1.perform(game1);
        action2.perform(game1);
        action3.perform(game2);


        action3.perform(game2);
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
        assertTrue(action1.isValid(game1));

        // try to grab ammo from a spawn tile
        assertFalse(action2.isValid(game1));

        // try to grab a weapon from spawn tile at distance 1
        assertTrue(action3.isValid(game1));

        // try to grab a weapon from normal tile
        assertFalse(action4.isValid(game1));

        // try to grab ammo at distance 2
        assertFalse(action5.isValid(game1));

        // try to grab ammo at distance 2 in final frenzy status (before first player)
        assertTrue(action6.isValid(game2));

        // try to grab ammo at distance 3 in final frenzy status (before first player)
        assertFalse(action7.isValid(game2));

        // try to grab ammo at distance 3 in final frenzy status (after first player)
        assertTrue(action8.isValid(game3));

        // try to do action with an invalid color
        assertFalse(action9.isValid(game3));
    }
}