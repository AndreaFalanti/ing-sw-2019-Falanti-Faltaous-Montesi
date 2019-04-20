package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;

import java.util.*;


public class Game {
    private Board mBoard;
    private ArrayList<Player> mPlayers;
    private Deck<PowerUpCard> mPowerUpCardDeck;
    private Deck<Weapon> mWeapons;
    private Deck<AmmoCard> mAmmoCardDeck;
    private int mTurnNumber;
    private int mSkullNum;
    private ArrayList<PlayerColor> mDeaths = new ArrayList<>();
    private boolean mFinalFrenzy;
    private Integer mFinalFrenzyTurnStart;
    private int mActivePlayer;

    public Game(Board board, ArrayList<Player> players, int killsToFinish) {
        if (board == null || killsToFinish < 0 || players.size() < 3) {
            throw new IllegalArgumentException();
        }
        mBoard = board;
        mPlayers = players;
        mSkullNum = killsToFinish;

        mFinalFrenzy = false;
        //set active player the last one in list so that at first turn it actually set it to 0
        mActivePlayer = mPlayers.size() - 1;
        mTurnNumber = 0;
    }

    public List<Player> getVisiblePlayers(Player player) {
        return null;
    }

    public void startNextTurn() {
        mTurnNumber++;

        if (mActivePlayer >= mPlayers.size() - 1) {
            mActivePlayer = 0;
        }
        else {
            mActivePlayer++;
        }

        //TODO: message to all clients containing new active player and turn number
    }

    public boolean isGameOver() {
        return false;
    }

    public int getTurnNumber() {
        return mTurnNumber;
    }

    public Deck<AmmoCard> getAmmoCardDeck() {
        return mAmmoCardDeck;
    }

    public Deck<Weapon> getWeapons() {
        return mWeapons;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }

    public Deck<PowerUpCard> getPowerUpDeck() {
        return mPowerUpCardDeck;
    }

    public Board getBoard() {
        return mBoard;
    }

    public int getSkullNum() {
        return mSkullNum;
    }

    public List<PlayerColor> getDeaths () {
        return mDeaths;
    }

    public void addDeath (PlayerColor killer) {
        mDeaths.add(killer);

        if (mDeaths.size() == mSkullNum) {
            setFinalFrenzyStatus();
        }
    }

    public Player getPlayerFromColor (PlayerColor color) { return null; }

    public boolean isFinalFrenzy () {
        return mFinalFrenzy;
    }

    public int getFinalFrenzyTurnStart () {
        return mFinalFrenzyTurnStart;
    }

    private void setFinalFrenzyStatus () {
        mFinalFrenzy = true;
        mFinalFrenzyTurnStart = mTurnNumber;
    }

}
