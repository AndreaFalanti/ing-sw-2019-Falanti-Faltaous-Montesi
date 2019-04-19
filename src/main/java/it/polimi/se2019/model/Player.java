package it.polimi.se2019.model;

import java.util.*;


public class Player {
    private AmmoValue mAmmo;
    private PowerUpCard[] mPowerUpCards = new PowerUpCard[4];
    private Weapon[] mWeapons = new Weapon[3];
    private PlayerColor mColor;
    private int mDeathsNum;
    private PlayerColor[] mDamageTaken = new PlayerColor[12];
    private EnumMap<PlayerColor, Integer> mMarks = new EnumMap<>(PlayerColor.class);
    private int mScore;
    private Position mPos;
    private String mName;
    private boolean mIsDead;

    private void initializeMarksMap () {
        mMarks.put(PlayerColor.YELLOW,0);
        mMarks.put(PlayerColor.GREEN,0);
        mMarks.put(PlayerColor.PURPLE,0);
        mMarks.put(PlayerColor.BLUE,0);
        mMarks.put(PlayerColor.GREY,0);
    }

    public static final int MAX_MARKS = 3;
    public Player (String name, PlayerColor color) {
            initializeMarksMap();
    }

    public void addScore(int value) {
        mScore += value;
    }

    public AmmoValue getAmmo() {
        return mAmmo;
    }

    public void sufferedDamage(PlayerColor attackingPlayer,int damage) {
        int i = 0;
        int j = 0;

        damage += getMarks().get(attackingPlayer);
        while(i <= mDamageTaken.length - 1 && mDamageTaken[i] != null)
            i++;
        if(i <= mDamageTaken.length - 1) {
            while(j <= damage - 1 && i <= mDamageTaken.length - 1) {
                mDamageTaken[i] = attackingPlayer;
                j++;
                i++;
            }
        }
        mMarks.put(attackingPlayer,0);
    }

    public PlayerColor[] getDamageTaken() {
        return mDamageTaken;
    }

    public void sufferedMarks(PlayerColor attackingPlayer,int marks) {
        if(marks + getMarks().get(attackingPlayer) >= 3) {
            mMarks.put(attackingPlayer,3);
        }
        else{
            mMarks.put(attackingPlayer,marks);
        }

    }

    public EnumMap<PlayerColor, Integer> getMarks() { return mMarks;}

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

    public void addWeapon(Weapon value) throws FullHandException {
        int i=0;

        while(i <= mWeapons.length - 1 && mWeapons[i] != null )
            i++;
        if(i <= mWeapons.length -1) {
            mWeapons[i] = value;
        }
        else {
            throw new FullHandException("You have reached the maximum number of weapons in your hand");
        }
    }

    public boolean getIsDead() {
        return mIsDead;
    }

    public void isDead(){
        if(mDamageTaken[11] != null) {
            mIsDead = true;
        }
        else {
            mIsDead = false;
        }
    }

    public PowerUpCard[] getPowerUps() {
        return mPowerUpCards;
    }

    public void addPowerUp(PowerUpCard value, boolean isRespawn) throws FullHandException {
        int lengthToCheck = isRespawn ? mPowerUpCards.length : (mPowerUpCards.length - 1);
        for (int i = 0; i < lengthToCheck; i++) {
            if (mPowerUpCards[i] == null) {
                mPowerUpCards[i] = value;
                return;
            }
        }

        throw new FullHandException ("PowerUp hand is full, can't draw another card");
    }

    public void addPowerUp (PowerUpCard value) throws FullHandException{
        addPowerUp(value, false);
    }

    public int getScore() {
        return mScore;
    }

    public Position getPos() {
        return mPos;
    }

    public void move(Position value) {
        mPos = value;
    }

    public String getName () {
        return  mName;
    }
}
