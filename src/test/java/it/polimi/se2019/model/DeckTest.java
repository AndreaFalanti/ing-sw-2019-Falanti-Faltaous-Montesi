package it.polimi.se2019.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        assertEquals(deck.getCards(),cards1);
        assertTrue(deck.getCards().containsAll(deck.getDeck()));
    }


    @Test
    public void testDrawCard() {
        ArrayList<String> cards1 = new ArrayList<>();
        cards1.add("weapon1");
        cards1.add("weapon2");
        cards1.add("weapon3");
        Deck<String> deck = new Deck<>(cards1);
        Deck<String> deck1 = new Deck<>(cards1,false);
        String drawnCard1 = deck.getDeck().get(deck.getDeck().size()-1);
        String drawnCard2 = deck.drawCard();
        assertEquals(drawnCard1 , drawnCard2);
        assertEquals(2,deck.getDeck().size());
        deck.drawCard();
        deck.drawCard();
        if(deck.isReshuffleable())
            deck.drawCard();
        assertEquals(2,deck.getDeck().size());
        deck1.drawCard();
        deck1.drawCard();
        deck1.drawCard();
        deck1.drawCard();
        assertEquals(0,deck1.getDeck().size());

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
        List<String> deck1 = deck.getDeck();

        deck.shuffle();
        assertTrue(deck.getCards().containsAll(deck.getDeck()));
    }

}