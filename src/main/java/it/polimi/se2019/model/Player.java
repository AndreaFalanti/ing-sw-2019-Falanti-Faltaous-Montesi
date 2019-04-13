package it.polimi.se2019.model;

import java.util.*;


public class Player {
    private AmmoValue mAmmo;
    private PowerUpCard[] mPowerUpCards;
    private Weapon[] mWeapons;
    private PlayerColor mColor;
    private int mDeathsNum;
    private PlayerColor[] mDamageTaken;
    private HashMap<PlayerColor, Integer> mMarks;
    private int mScore;
    private Position mPos;
    private String mName;

    public Player (String name, PlayerColor color) {

    }


    public void addScore(int value) { }

    public AmmoValue getAmmo() {
        return null;
    }

    public void addAmmo(AmmoValue value) { }

    public PlayerColor getColor() {
        return null;
    }

    public int getDeathsNum() {
        return 0;
    }

    public void incrementDeaths() {
    }

    public Weapon[] getWeapons() {
        return null;
    }

    public void addWeapon(Weapon value) {
    }

    public PowerUpCard getPowerUps() {
        return null;
    }

    public void addPowerUp(PowerUpCard value) {
    }

    public int getScore() {
        return 0;
    }

    public Position getPos() {
        return null;
    }

    public void move(Position value) {
    }

    public String getName () { return  null;}

}
