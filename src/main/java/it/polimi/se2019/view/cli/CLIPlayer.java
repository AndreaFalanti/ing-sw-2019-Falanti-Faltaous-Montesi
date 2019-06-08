package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.*;
import it.polimi.se2019.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CLIPlayer {

    private String mPlayerName ;
    private String mPlayerColor ;
    private String mPlayerScore;
    private String dead;
    private String overkilled;
    private String mPlayerWeapons ;
    private String mPlayerPos;
    private Map<String,Integer> mPlayerMarks;
    private String mDamageTaken;
    private String mPlayerPowerUps;
    private String mDeathNum;
    private String mPlayerAmmo;
    private String mBoardIsFlipped ;

    public CLIPlayer(Player player){

            mPlayerName = player.getName();
            mPlayerColor = player.getColor().getPascalName();
            mPlayerScore = "0";
            setAmmo(player.getAmmo());
            dead = "is not dead";//to update
            overkilled = "not is overkilled";//to update
            mBoardIsFlipped="false";
            mDeathNum = "0";
            setWeaponOtherPlayer(player.getWeapons());
            setPosition(player.getPos());
            setScore(player.getScore());
            setWeaponOwner(player.getWeapons());
            for(Map.Entry<PlayerColor,Integer> entry: player.getMarks().entrySet()){
                mPlayerMarks.put(player.getColor().getPascalName(),player.getMarks().get(player.getColor()));
            }

        setMarks(player.getMarks().get(player.getColor()),player.getColor());

    }

    public String getPlayerColor(){return mPlayerColor;}

    public String getPlayerName(){return mPlayerName;}

    public String getPlayerPowerUps(){return mPlayerPowerUps;}

    public String getPlayerAmmo(){return mPlayerAmmo;}

    public String getPlayerDeaths(){return mDeathNum;}

    public String getPlayerDamage(){return mDamageTaken;}

    public Map<String, Integer> getPlayerMarks(){return mPlayerMarks;}

    public String playerIsDead(){return dead;}

    public String playerIsOverkilled(){return overkilled;}

    public String getPlayerPosition(){return mPlayerPos;}

    public String getPlayerWeapons(){return mPlayerWeapons;}

    public String getPlayerScore(){return mPlayerScore;}


    public void setScore(int i){
        mPlayerScore = String.valueOf(i);
    }

    public void setDeathNums(){
        mDeathNum = String.valueOf(Integer.parseInt(mDeathNum) + 1);
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

    public void setPowerUpsOwnerPlayer(PowerUpCard[] powerUpCards){
        StringBuilder power = new StringBuilder();

        if (powerUpCards == null){
            mPlayerPowerUps = "not have power up card !";
            return;
        }

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

        mPlayerMarks.put(shooterPlayerColor.getPascalName(),marks);
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
