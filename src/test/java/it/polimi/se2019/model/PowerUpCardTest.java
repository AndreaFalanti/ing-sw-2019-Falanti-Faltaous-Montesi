package it.polimi.se2019.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PowerUpCardTest {
    @Test
    public void testPowerUpCardConstructor () {
        try {
            PowerUpCard card =
                    new PowerUpCard("Teleport", new AmmoValue(0,0,1), new TeleportBehaviour());
            assertTrue(true);
        }
        catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testPowerUpCardConstructorIllegalArgumentException () {
        try {
            PowerUpCard card =
                    new PowerUpCard("Teleport", new AmmoValue(1,0,1), new TeleportBehaviour());
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            PowerUpCard card =
                    new PowerUpCard("Teleport", new AmmoValue(0,0,0), new TeleportBehaviour());
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetColor() {
        PowerUpCard card = new PowerUpCard("Newton", new AmmoValue(0,1,0), null);
        TileColor color = card.getColor();
        assertEquals(TileColor.YELLOW, color);
    }
}