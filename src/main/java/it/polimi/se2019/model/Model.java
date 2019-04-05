package it.polimi.se2019.model;

import java.util.*;


public class Model {

    private Board board;

    private Player players;

    private Deck<PowerUpCard> powerUpDeck;

    private Deck<Weapon> weapons;

    private Deck<AmmoCard> ammoCardDeck;

    private int turnNumber;

    private int skullNum;

    private PlayerColor deaths;


    public Player getVisiblePlayers(Player player) {
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

    public Player getPlayers() {
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

    public Player getPlayerFromColor (PlayerColor color) { return null; }

}
