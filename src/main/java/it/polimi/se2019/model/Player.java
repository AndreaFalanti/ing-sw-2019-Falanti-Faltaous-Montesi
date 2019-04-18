package it.polimi.se2019.model;

import java.util.*;


public class Player {
    private AmmoValue mAmmo;
    private PowerUpCard[] mPowerUpCards;
    private Weapon[] mWeapons = new Weapon[3];
    private PlayerColor mColor;
    private int mDeathsNum;
    private PlayerColor[] mDamageTaken = new PlayerColor[12];
    private HashMap<PlayerColor, Integer> mMarks = new HashMap<PlayerColor, Integer>(){{
        put(PlayerColor.YELLOW,0);
        put(PlayerColor.GREEN,0);
        put(PlayerColor.PURPLE,0);
        put(PlayerColor.BLUE,0);
        put(PlayerColor.GREY,0);
        }
    };
    private int mScore;
    private Position mPos;
    private String mName;
    private boolean mIsDead;

    public Player (String name, PlayerColor color) {

    }


    public void addScore(int value) {
        this.mScore += value;
    }

    public AmmoValue getAmmo() {
        return mAmmo;
    }

    public void sufferedDamage(PlayerColor attackingPlayer,int damage) {
        int i = 0;
        int j = 0;

        while(i <= mDamageTaken.length - 1 && mDamageTaken[i] != null)
            i++;
        if(i <= mDamageTaken.length - 1) {
            while(j <= damage - 1 && i <= mDamageTaken.length - 1) {
                mDamageTaken[i] = attackingPlayer;
                j++;
                i++;
            }
        }
    }

    public PlayerColor[] getDamageTaken() { return mDamageTaken;}

    public void sufferedMarks(PlayerColor attackingPlayer,int mark) { }

    public HashMap<PlayerColor, Integer> getMarks() { return mMarks;}

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

    public boolean getIsDead() { return mIsDead; }

    public void isDead(){

    }

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
