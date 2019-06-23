package it.polimi.se2019.model.action;

import it.polimi.se2019.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class AmmoPaymentTest {

    @Test
    public void testPayCost() {
        Player player = new Player("a", PlayerColor.GREY);
        player.addPowerUp(new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,0,1)));
        AmmoValue cost1 = new AmmoValue(1,1,0);
        AmmoValue cost2 = new AmmoValue(1,1,2);
        boolean[] discardedCards1 = {false, false, false};
        boolean[] discardedCards2 = {true, false, false};

        AmmoPayment.payCost(player, cost1, discardedCards1);
        assertEquals(new AmmoValue(0,0,1), player.getAmmo());

        player.getAmmo().add(cost1);

        AmmoPayment.payCost(player, cost2, discardedCards2);
        assertEquals(new AmmoValue(0,0,0), player.getAmmo());
    }

    @Test
    public void testIsValid() {
        Player player = new Player("a", PlayerColor.GREY);
        player.addPowerUp(new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,0,1)));
        AmmoValue cost1 = new AmmoValue(1,1,0);
        AmmoValue cost2 = new AmmoValue(1,1,2);
        boolean[] discardedCards1 = {false, false, false};
        boolean[] discardedCards2 = {true, false, false};
        boolean[] discardedCards3 = {true, true, true};

        assertTrue(AmmoPayment.isValid(player, cost1, discardedCards1));
        assertTrue(AmmoPayment.isValid(player, cost2, discardedCards2));

        // can't cover cost without discarding cards
        assertFalse(AmmoPayment.isValid(player, cost2, discardedCards1));

        // can't discard null cards
        assertFalse(AmmoPayment.isValid(player, cost2, discardedCards3));
    }

    @Test
    public void testAddAmmoAndDiscard() {
        Player player = new Player("a", PlayerColor.GREY);
        player.addPowerUp(new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(1,0,0)));
        player.addPowerUp(new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0)));
        player.addPowerUp(new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,0,1)));

        boolean[] discardedCards1 = {false, true, true};
        boolean[] discardedCards2 = {true, false, false};
        boolean[] discardedCards3 = {false, false, false};

        AmmoPayment.addAmmoAndDiscard(player, discardedCards1);
        assertEquals(new AmmoValue(1,2,2), player.getAmmo());

        AmmoPayment.addAmmoAndDiscard(player, discardedCards2);
        assertEquals(new AmmoValue(2,2,2), player.getAmmo());

        AmmoPayment.addAmmoAndDiscard(player, discardedCards3);
        assertEquals(new AmmoValue(2,2,2), player.getAmmo());
    }
}