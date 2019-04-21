package it.polimi.se2019.model;

import it.polimi.se2019.model.board.Board;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class Game {
    private Board mBoard;
    private ArrayList<Player> mPlayers;
    private Deck<PowerUpCard> mPowerUpCardDeck;
    private Deck<Weapon> mWeapons;
    private Deck<AmmoCard> mAmmoCardDeck;
    private int mTurnNumber;
    private int mSkullNum;
    private ArrayList<PlayerColor> mKills = new ArrayList<>();
    private boolean mFinalFrenzy;
    private Integer mFinalFrenzyTurnStart;
    private int mActivePlayer;

    private static final int[] KILLS_VALUE = {8, 6, 4, 2, 1, 1};

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
        if (isGameOver()) {
            distributeKillScore();
            return;
        }

        if (mActivePlayer >= mPlayers.size() - 1) {
            mActivePlayer = 0;
        }
        else {
            mActivePlayer++;
        }

        //TODO: message to all clients containing new active player and turn number
    }

    public boolean isGameOver() {
        // example: finalFrenzy is triggered on turn 4 and 3 player are present,
        // at turn 4 + 3 + 1 = 8 the game is over (last turn is 7).
        return mFinalFrenzyTurnStart != null
                && mTurnNumber >= mFinalFrenzyTurnStart + mPlayers.size() + 1;
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

    public List<PlayerColor> getKills() {
        return mKills;
    }

    public void addDeath (PlayerColor killer) {
        mKills.add(killer);

        if (mKills.size() == mSkullNum) {
            setFinalFrenzyStatus();
        }
    }

    public Player getPlayerFromColor (PlayerColor color) {
        for (Player player : mPlayers) {
            if (player.getColor() == color) {
                return player;
            }
        }

        throw new IllegalArgumentException("Can't find player with color: " + color);
    }

    public boolean isFinalFrenzy () {
        return mFinalFrenzy;
    }

    public Integer getFinalFrenzyTurnStart () {
        return mFinalFrenzyTurnStart;
    }

    private void setFinalFrenzyStatus () {
        mFinalFrenzy = true;
        mFinalFrenzyTurnStart = mTurnNumber;
    }

    private void distributeKillScore () {
        Map<PlayerColor, Integer> map = new EnumMap<>(PlayerColor.class);
        for (PlayerColor kill : mKills) {
            if (map.containsKey(kill)) {
                map.put(kill, map.get(kill) + 1);
            }
            else {
                map.put(kill, 1);
            }
        }
        AtomicInteger i = new AtomicInteger(0);
        map
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEachOrdered( e -> {getPlayerFromColor(e.getKey()).addScore(KILLS_VALUE[i.get()]);
                                        i.getAndIncrement();});

    }

}
