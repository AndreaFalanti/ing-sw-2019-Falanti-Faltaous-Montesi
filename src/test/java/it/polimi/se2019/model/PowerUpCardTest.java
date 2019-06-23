package it.polimi.se2019.model;

import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.util.Jsons;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PowerUpCardTest {
    private static final int EXPECTED_CARDS_NUM = 24;

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testPowerUpCardConstructor () {
        try {
            PowerUpCard card =
                    new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,0,1));
            assertTrue(true);
        }
        catch (IllegalArgumentException e) {
            fail();
        }
    }

    /**
     * Test constructor with invalid parameters, it should throw an exception.
     */
    @Test
    public void testPowerUpCardConstructorIllegalArgumentException () {
        try {
            PowerUpCard card =
                    new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(1,0,1));
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            PowerUpCard card =
                    new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,0,0));
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test that color returned is matching the not zero value contained in ammoValue.
     */
    @Test
    public void testGetColor() {
        PowerUpCard card = new PowerUpCard(PowerUpType.NEWTON, new AmmoValue(0,1,0));
        TileColor color = card.getColor();
        assertEquals(TileColor.YELLOW, color);

        PowerUpCard card2 = new PowerUpCard(PowerUpType.NEWTON, new AmmoValue(1,0,0));
        TileColor color2 = card2.getColor();
        assertEquals(TileColor.RED, color2);

        PowerUpCard card3 = new PowerUpCard(PowerUpType.NEWTON, new AmmoValue(0,0,1));
        TileColor color3 = card3.getColor();
        assertEquals(TileColor.BLUE, color3);
    }

    @Test
    public void returnDeckFromJson() {
        String json = Jsons.get("PowerUpCardDeck");

        List<PowerUpCard> deck = PowerUpCard.returnDeckFromJson(json);
        assertEquals(EXPECTED_CARDS_NUM, deck.size());

        for (PowerUpCard powerUpCard : deck) {
            assertNotNull(powerUpCard);
            assertNotNull(powerUpCard.getType());
            assertNotNull(powerUpCard.getAmmoValue());
            assertNotNull(powerUpCard.getGuiID());
        }
    }
}