package it.polimi.se2019.model;

import java.util.*;

public class Deck<T> {
    private ArrayList<T> mDeck ;
    private ArrayList<T> mCards ;

    public Deck(ArrayList<T> cards) {
        this.mCards = cards;
        shuffle();
    }

    public T drawCard() {

        T drawnCard;

        if(mDeck == null)
               shuffle();

        drawnCard = mDeck.get(mDeck.size() - 1);
        mDeck.remove(mDeck.size() - 1);

        return drawnCard;
    }

    public void shuffle() {

        this.mDeck = mCards ;
        Collections.shuffle(mDeck);
    }

    public ArrayList<T> getCards(){
        return mCards;
    }

    public ArrayList<T> getDeck(){
        return mDeck;
    }

}
