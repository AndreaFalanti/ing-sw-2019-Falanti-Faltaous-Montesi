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


    public void addScore(int value) {
        this.mScore += value;
    }

    public AmmoValue getAmmo() {
        return mAmmo;
    }

    //public void addAmmo(AmmoValue value) { }

    public PlayerColor getColor() {
        return mColor;
    }

    public int getDeathsNum() {
        return mDeathsNum;
    }

    public void incrementDeaths() {
        mDeathsNum += 1;
    }

    public Weapon[] getWeapons() {
        return mWeapons;
    }

    public void addWeapon(Weapon value) { }

    public PowerUpCard getPowerUps() {
        return null;
    }

    public void addPowerUp(PowerUpCard value) { }

    public int getScore() {
        return mScore;
    }

    public Position getPos() {
        return mPos;
    }

    public void move(Position value) { }

    public String getName () { return  mName;}

}
