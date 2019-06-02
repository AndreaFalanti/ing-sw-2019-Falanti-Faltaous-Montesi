package it.polimi.se2019.model;

import it.polimi.se2019.util.Jsons;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AmmoCardTest {
    private static final int EXPECTED_CARDS_NUM = 36;

    /**
     * Test that all ammoCards are instantiated from json and not null
     */
    @Test
    public void testReturnDeckFromJson() {
        String json = Jsons.get("AmmoCardDeck");

        List<AmmoCard> deck = AmmoCard.returnDeckFromJson(json);
        assertEquals(EXPECTED_CARDS_NUM, deck.size());

        for (AmmoCard ammocard : deck) {
            assertNotNull(ammocard);
            assertNotNull(ammocard.getAmmoGain());
            assertNotNull(ammocard.getGuiID());
        }
    }
}