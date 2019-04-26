package it.polimi.se2019.model;

import java.util.Collections;
import java.util.List;

public class Deck<T> {
    private List<T> mDeck ;
    private List<T> mCards ;

    public Deck(List<T> cards) {
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

    public List<T> getCards(){
        return mCards;
    }

    public List<T> getDeck(){
        return mDeck;
    }

}
