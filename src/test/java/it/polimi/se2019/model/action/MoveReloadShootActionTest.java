package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.AmmoValue;
import it.polimi.se2019.model.Game;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveReloadShootActionTest {

    @Test
    public void testPerform() {
        Game game = GameTestCaseBuilder.generateFinalFrenzyGameAfterFirstPlayer();
        Weapon weapon = new Weapon("a", new AmmoValue(), new AmmoValue());
        weapon.setLoaded(false);
        game.getActivePlayer().addWeapon(weapon);

        Position position = new Position(2, 0);
        MoveReloadShootAction action = new MoveReloadShootAction(game.getActivePlayer().getColor(),
                position, 0, 0);
        action.perform(game);
        assertFalse(weapon.isLoaded());
        assertEquals(position, game.getActivePlayer().getPos());
    }

    @Test
    public void testIsValid() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        Weapon weapon = new Weapon("a", new AmmoValue(), new AmmoValue());
        weapon.setLoaded(false);
        game.getActivePlayer().addWeapon(weapon);

        MoveReloadShootAction action = new MoveReloadShootAction(game.getActivePlayer().getColor(),
                new Position(2, 0), 0, 0);
        //can't use action in a non frenzy game
        assertTrue(action.getErrorResponse(game).isPresent());

        Game game1 = GameTestCaseBuilder.generateFinalFrenzyGameBeforeFirstPlayer();
        game1.getActivePlayer().addWeapon(weapon);
        Game game2 = GameTestCaseBuilder.generateFinalFrenzyGameAfterFirstPlayer();
        game2.getActivePlayer().addWeapon(weapon);
        // can't move 2 spaces if playing before first player
        assertTrue(action.getErrorResponse(game1).isPresent());
        // can move 2 spaces if playing after first player
        assertFalse(action.getErrorResponse(game2).isPresent());
    }
}