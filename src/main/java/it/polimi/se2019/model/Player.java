package it.polimi.se2019.model;

import java.util.*;


public class Player {

    private AmmoValue ammo;

    private PowerUpCard[] powerUps;

    private Weapon[] weapons;

    private PlayerColor color;

    private int deathsNum;

    private PlayerColor[] damageTaken;

    private HashMap<PlayerColor, Integer> marks;

    private int score;

    private Position pos;

    private String name;


    public void addScore(int value) { }

    public AmmoValue getAmmo() {
        return null;
    }

    public void setAmmo(AmmoValue value) { }

    public PlayerColor getColor() {
        return null;
    }

    public int getDeathsNum() {
        return 0;
    }

    public void setDeathsNum(int value) {
    }

    public Weapon[] getWeapons() {
        return null;
    }

    public void addWeapon(Weapon value) {
    }

    public PowerUpCard getPowerUps() {
        return null;
    }

    public void setPowerUps(PowerUpCard value) {
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
