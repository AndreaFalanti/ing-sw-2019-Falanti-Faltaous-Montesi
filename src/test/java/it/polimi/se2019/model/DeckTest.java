package it.polimi.se2019.model;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class DeckTest {

    @Test
    public void testConstruction() {
        ArrayList<String> cards1 = new ArrayList<>();
        cards1.add("weapon1");
        cards1.add("weapon2");
        cards1.add("weapon3");
        cards1.add("weapon4");
        cards1.add("weapon5");
        Deck<String> deck = new Deck<>(cards1);

        assertEquals(deck.getMCards(),cards1);
        assertTrue(deck.getMCards().containsAll(deck.getMDeck()));
    }


    @Test
    public void testDrawCard() {
        ArrayList<String> cards1 = new ArrayList<>();
        cards1.add("weapon1");
        cards1.add("weapon2");
        cards1.add("weapon3");
        cards1.add("weapon4");
        cards1.add("weapon5");
        Deck<String> deck = new Deck<>(cards1);

        String drawnCard = deck.drawCard();
        assertEquals("weapon5" , drawnCard);
        assertEquals(4,deck.getMDeck().size());
    }

    @Test
    public void testShuffle() {
        ArrayList<String> cards1 = new ArrayList<>();
        cards1.add("weapon1");
        cards1.add("weapon2");
        cards1.add("weapon3");
        cards1.add("weapon4");
        cards1.add("weapon5");
        Deck<String> deck = new Deck<>(cards1);
        ArrayList<String> deck1 = deck.getMDeck();

        deck.shuffle();
        assertTrue(deck.getMCards().containsAll(deck.getMDeck()));
        assertNotEquals(deck.getMDeck(),deck1);
    }

}