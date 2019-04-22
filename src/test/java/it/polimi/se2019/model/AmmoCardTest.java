package it.polimi.se2019.model;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AmmoCardTest {
    private static final int EXPECTED_CARDS_NUM = 36;

    /**
     * Test that all ammoCards are instantiated from json and not null
     */
    @Test
    public void testReturnDeckFromJson() {
        String json = null;
        try {
            json = new String (Files.readAllBytes(Paths.get(ResourcePaths.AMMO_CARDS_JSON)));
        }
        catch (IOException e){
            fail();
        }

        List<AmmoCard> deck = AmmoCard.returnDeckFromJson(json);
        assertEquals(EXPECTED_CARDS_NUM, deck.size());

        for (AmmoCard ammocard : deck) {
            assertNotNull(ammocard);
            assertNotNull(ammocard.getAmmoGain());
        }
    }
}