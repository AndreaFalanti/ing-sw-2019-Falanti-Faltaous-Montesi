package it.polimi.se2019.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck<T> {
    private List<T> mDeck;
    private List<T> mCards;
    private boolean mReshuffleable;

    public Deck(List<T> cards) {
        mCards = cards;
        mReshuffleable = true;
        shuffle();
    }

    /**
     * Complete constructor
     * @param cards Cards to use in deck
     * @param canBeReshuffled Set if it can be recreated and shuffled from waste stack when it's empty
     */
    public Deck(List<T> cards, boolean canBeReshuffled) {
        this(cards);
        mReshuffleable = canBeReshuffled;
    }

    /**
     * Draw a card from deck
     * @return Drawn card
     */
    public T drawCard() {
        T drawnCard;

        if(mDeck.isEmpty() && mReshuffleable)
            shuffle();

        if (mDeck.isEmpty()) {
            return null;
        }
        else {
            drawnCard = mDeck.get(mDeck.size() - 1);
            mDeck.remove(mDeck.size() - 1);

            return drawnCard;
        }
    }

    /**
     * Recreate deck from discarded cards and shuffle all cards in random order
     */
    public void shuffle() {
        mDeck = copyCard();
        Collections.shuffle(mDeck);
    }

    public List<T> copyCard(){
        return new ArrayList<>(this.getCards());
    }

    public List<T> getCards(){
        return mCards;
    }

    public List<T> getDeck(){
        return mDeck;
    }

    public boolean isReshuffleable() {
        return mReshuffleable;
    }
}
