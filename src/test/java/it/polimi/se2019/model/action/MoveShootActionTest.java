package it.polimi.se2019.model.action;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;
import it.polimi.se2019.util.GameTestCaseBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveShootActionTest {

    @Test
    public void testPerform() {
        Game game = GameTestCaseBuilder.generateFinalFrenzyGameBeforeFirstPlayer();
        Weapon weapon = new Weapon("a", new AmmoValue(), new AmmoValue());
        weapon.setLoaded(true);
        game.getActivePlayer().addWeapon(weapon);

        MoveShootAction action = new MoveShootAction(game.getActivePlayer().getColor(), new Position(2,0), 0);
        action.perform(game);
        assertFalse(weapon.isLoaded());
        assertEquals(new Position(2, 0), game.getActivePlayer().getPos());
    }

    @Test
    public void testIsValid() {
        Game game = GameTestCaseBuilder.generateBaseGame();
        Weapon weapon = new Weapon("a", new AmmoValue(), new AmmoValue());
        weapon.setLoaded(true);
        game.getActivePlayer().addWeapon(weapon);

        MoveShootAction action1 = new MoveShootAction(game.getActivePlayer().getColor(), new Position(1,0), 0);
        MoveShootAction action2 = new MoveShootAction(game.getActivePlayer().getColor(), new Position(2,0), 0);

        // can't move because is not damaged
        assertTrue(action1.getErrorResponse(game).isPresent());
        game.handleDamageInteraction(PlayerColor.GREY, game.getActivePlayer().getColor(), new Damage(6, 0));
        // now has access to adrenaline move shoot
        assertFalse(action1.getErrorResponse(game).isPresent());
        // but can only move one space
        assertTrue(action2.getErrorResponse(game).isPresent());

        Game game1 = GameTestCaseBuilder.generateFinalFrenzyGameAfterFirstPlayer();
        game1.getActivePlayer().addWeapon(weapon);
        // in frenzy a player that is playing after (or as) first player can move 2 spaces while shooting
        assertFalse(action2.getErrorResponse(game1).isPresent());
    }
}