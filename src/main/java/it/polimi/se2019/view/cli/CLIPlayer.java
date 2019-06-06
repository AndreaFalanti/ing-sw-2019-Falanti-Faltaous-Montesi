package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class CLIPlayer {

    private String mOwnerName;
    private String mOwnerColorf;
    private String mActivePlayerf;
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
    private String mPlayerAmmo;

    public CLIPlayer(List<Player> players){
        for(Player player: players) {
            mPlayerName = player.getName();
            mPlayerColor = player.getColor().getPascalName();
            mPlayerScore = String.valueOf(player.getScore());

            isDead = String.valueOf(player.isDead());
            isOverkilled = String.valueOf(player.isOverkilled());
            setWeaponOtherPlayer(player.getColor(),player.getWeapons());
            setPosition(player.getColor(),player.getPos());
        }
    }

    public void setWeaponOtherPlayer(PlayerColor color, Weapon[] weapons){//to complete with color
        List<String> nameWeapon = new ArrayList<>();
        String load;
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


    public void setPosition(PlayerColor color, Position pos){//to complete with color
        List<String> positionPlayer= new ArrayList<>();
        if (pos==null)
            mPlayerPos = "not respawned";
        else {
            positionPlayer.add(String.valueOf(pos.getX()));
            positionPlayer.add(String.valueOf(pos.getY()));
            mPlayerPos = String.join(",",positionPlayer);
        }
    }

    public void setMarks(Player player){
        //todo
    }

    public void setDamageTaken(PlayerColor damagedPlayer,int damage, PlayerColor attackingColor){
        List<String> damageTaken = new ArrayList<>();
       //todo
    }


    }
