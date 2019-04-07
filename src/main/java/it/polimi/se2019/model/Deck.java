package it.polimi.se2019.model;

import java.util.*;

public class Deck<T> {

    private ArrayList<T> deck = new ArrayList<>();
    private ArrayList<T> cards = new ArrayList<>();


    public T drawCard() {

        T drawedCard;

        if(deck == null)
               shuffle(deck);

        drawedCard = deck.get(deck.size() - 1);
        deck.remove(deck.size() - 1);

        return drawedCard;
    }

    public void shuffle(ArrayList<T> deck) {

        deck = cards ;
        Collections.shuffle(deck);
    }

}
