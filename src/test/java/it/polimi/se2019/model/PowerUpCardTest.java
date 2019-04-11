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

        PowerUpCard card2 = new PowerUpCard("Newton", new AmmoValue(1,0,0), null);
        TileColor color2 = card2.getColor();
        assertEquals(TileColor.RED, color2);

        PowerUpCard card3 = new PowerUpCard("Newton", new AmmoValue(0,0,1), null);
        TileColor color3 = card3.getColor();
        assertEquals(TileColor.BLUE, color3);
    }
}