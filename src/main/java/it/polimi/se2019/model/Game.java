package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;

import java.util.*;


public class Game {
    private Board mBoard;
    private Player[] mPlayers;
    private Deck<PowerUpCard> mPowerUpCardDeck;
    private Deck<Weapon> mWeapons;
    private Deck<AmmoCard> mAmmoCardDeck;
    private int mTurnNumber;
    private int mSkullNum;
    private ArrayList<PlayerColor> mDeaths;
    private boolean mFinalFrenzy;
    private Integer mFinalFrenzyTurnStart;

    public Game(Board board, Player[] players, int killsToFinish) {
        
    }

    public Player[] getVisiblePlayers(Player player) {
        return null;
    }

    public void startNextTurn() {
    }

    public boolean isGameOver() {
        return false;
    }

    public int getTurnNumber() {
        return 0;
    }

    public Deck<AmmoCard> getAmmoCardDeck() {
        return null;
    }

    public Deck<Weapon> getWeapons() {
        return null;
    }

    public Player[] getPlayers() {
        return null;
    }

    public Deck<PowerUpCard> getPowerUpDeck() {
        return null;
    }

    public Board getBoard() {
        return null;
    }

    public int getSkullNum() {
        return 0;
    }

    public void setSkullNum(int value) {
    }

    public ArrayList<PlayerColor> getDeaths () {
        return mDeaths;
    }

    public void addDeath (PlayerColor killer) {

    }

    public Player getPlayerFromColor (PlayerColor color) { return null; }

    public boolean isFinalFrenzy () {
        return mFinalFrenzy;
    }

    public int getFinalFrenzyTurnStart () {
        return mFinalFrenzyTurnStart;
    }

}
