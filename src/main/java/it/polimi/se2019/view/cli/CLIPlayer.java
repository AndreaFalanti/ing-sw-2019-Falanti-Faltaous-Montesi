package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class CLIPlayer {

    private String mPlayerName ;
    private String mPlayerColor ;
    private String mPlayerScore;
    private String isDead;
    private String isOverkilled;
    private String mPlayerWeapons ;
    private String mPlayerPos;
    private String mPlayerMarks;
    private String mDamageTaken;
    private String mPlayerPowerUps;
    private String mDeathNums;
    private String mPlayerAmmo;
    private String mBoardIsFlipped ;

    public CLIPlayer(List<Player> players){
        for(Player player: players) {
            mPlayerName = player.getName();
            mPlayerColor = player.getColor().getPascalName();
            mPlayerScore = "0";
            setAmmo(player.getAmmo());
            //isDead
            //isOverkilled
            mBoardIsFlipped="false";
            mDeathNums="0";
            setWeaponOtherPlayer(player.getWeapons());
            setPosition(player.getPos());
        }
    }


    public void setScore(int i){
        mPlayerScore = String.valueOf(i);
    }

    public void setDeathNums(){
        mDeathNums = String.valueOf(Integer.parseInt(mDeathNums) + 1);
    }

    public void flipBoard(){
        mBoardIsFlipped = "true";
    }

    public void setAmmo(AmmoValue ammo){
        mPlayerAmmo = ammo.toString();
    }

    public void setPowerUpsOtherPlayers(int i){
        mPlayerPowerUps = String.valueOf(i);
    }

    public void setPowerUpsOwnerPlayers(PowerUpCard[] powerUpCards){
        StringBuilder power = new StringBuilder();

        for (PowerUpCard powerUpCard : powerUpCards) {
            power.append("PowerUpCard{ ");
            power.append("Name= ");
            power.append(powerUpCard.getName());
            power.append(" ");
            power.append("AmmoValue= ");
            power.append(powerUpCard.getAmmoValue().toString());
            power.append("}");
        }

        mPlayerPowerUps = power.toString();
    }

    public void setMarks(int marks,PlayerColor shooterPlayerColor){
        StringBuilder mark = new StringBuilder();
        mark.append(mDamageTaken);
        for(int i=0 ; i<marks; i++)
            mark.append(shooterPlayerColor.getPascalName());
        mPlayerMarks = mark.toString();
    }


    public void setDamageTaken(int damageTaken, PlayerColor shooterPlayerColor){
        StringBuilder damage = new StringBuilder();
        damage.append(mDamageTaken);
        for(int i=0 ; i<damageTaken; i++)
            damage.append(shooterPlayerColor.getPascalName());
        mDamageTaken = damage.toString();
    }

    public void setWeaponOtherPlayer(Weapon[] weapons){
        List<String> nameWeapon = new ArrayList<>();
        if (weapons == null)
            mPlayerWeapons = "not have weapon";
        else{
            for(Weapon weapon: weapons){
                if(weapon.isLoaded())
                    nameWeapon.add(weapon.getName());
            }
            mPlayerWeapons = String.join(",",nameWeapon);
        }
    }

    public void setWeaponOwner(Weapon[] weapons){
        List<String> nameWeapon = new ArrayList<>();
        String load = "";
        if (weapons == null)
            mPlayerWeapons = "not have weapon";
        else{
            for(Weapon weapon: weapons){
                if(!weapon.isLoaded())
                    load = "(to load)";
                nameWeapon.add(weapon.getName() + load);
            }
            mPlayerWeapons = String.join(",",nameWeapon);
        }
    }


    public void setPosition(Position pos){
        if (pos==null)
            mPlayerPos = "not respawned";
        else {
            mPlayerPos =  pos.toString();
        }
    }




}
