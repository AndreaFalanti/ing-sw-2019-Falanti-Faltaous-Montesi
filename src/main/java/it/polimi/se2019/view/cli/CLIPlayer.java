package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;

import java.util.*;
import java.util.stream.Collectors;

public class CLIPlayer {
    private static final String SPACE   =  "\n\t\t\t\t\t\t";
    private static final String NOWEAPON = "not have weapon";
    private String mPlayerName ;
    private String mPlayerColor ;
    private String mPlayerScore;
    private String dead;
    private String overkilled;
    private List<String> mPlayerWeapons =new  ArrayList<>();
    private Map<String,String> weaponsInfo =new HashMap<>();
    private String mPlayerPos;
    private Map<String,Integer> mPlayerMarks = new HashMap<>();
    private String mDamageTaken;
    private String mPlayerPowerUps;
    private String mDeathNum;
    private String mPlayerAmmo;
    private String mBoardFlipped;

    public CLIPlayer(Player player,PlayerColor ownerColor){
    int count=0;
            mPlayerName = player.getName();
            mPlayerColor = player.getColor().getPascalName();
            setScore(player.getScore());
            setAmmo(player.getAmmo());
            setDead(player.isDead());
            setOverkilled(player.isOverkilled());
            mDeathNum = String.valueOf(player.getDeathsNum());
            setPosition(player.getPos());
            setScore(player.getScore());
            if(player.getColor().getPascalName().equals(ownerColor.getPascalName())){
                setWeaponOwner(player.getWeapons());
                setPowerUpsOwnerPlayer(player.getPowerUps());
            }
            else{
                setWeaponOtherPlayer(player.getWeapons());
                for(int i = 0;i<player.getPowerUps().length;i++)
                    if(player.getPowerUps()[i]!=null)
                        count+=1;
                setPowerUpsOtherPlayers(count);
            }
            for(Map.Entry<PlayerColor,Integer> entry: player.getMarks().entrySet()){
                setMarks(entry.getValue(),entry.getKey());
            }
            setAllDamageTaken(player.getDamageTaken());
            setBoardFlipped(player.isBoardFlipped());

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

    public List<String> getPlayerWeapons(){return mPlayerWeapons;}

    public Map<String, String> getWeaponsInfo(){return weaponsInfo;}

    public String getPlayerScore(){return mPlayerScore;}

    public String getBoardFlipped(){return mBoardFlipped;}


    public void setScore(int i){
        mPlayerScore = String.valueOf(i);
    }

    public void setDeathNums(){
        mDeathNum = String.valueOf(Integer.parseInt(mDeathNum) + 1);
    }

    public void setBoardFlipped(boolean flipped){
        if(flipped)
            mBoardFlipped = "Is flipped";
        else
            mBoardFlipped = "Is not flipped";
    }

    public void setBoardFlipped(){
        mBoardFlipped = "Is flipped";
    }

    public void setAmmo(AmmoValue ammo){

        mPlayerAmmo =   Colors.getColorTile("red") + "Red " + Colors.ANSI_RESET + ammo.getRed()+" "+
                        Colors.getColorTile("yellow") + "Yellow " +Colors.ANSI_RESET+ ammo.getYellow()+" "+
                        Colors.getColorTile("blue") + "Blue "+Colors.ANSI_RESET + ammo.getBlue();
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
        power.append("PowerUpCard: ");
        for (PowerUpCard powerUpCard : powerUpCards) {
            if(powerUpCard != null){
                power.append("Name : ");
                power.append(powerUpCard.getType());
                power.append(" ");
                power.append("Color: ");
                power.append(Colors.getColorTile(powerUpCard.getColor().getPascalName()));
                power.append(powerUpCard.getColor().getPascalName());
                power.append(Colors.ANSI_RESET);
                power.append(SPACE+" ");
            }
        }

        mPlayerPowerUps = power.toString();
    }

    public void setDead(boolean isDead){
        if(!isDead)
            dead = "false";
        else
            dead = "true";
    }

    public void setOverkilled(boolean isOverkilled){
        if(!isOverkilled)
            overkilled = "false";
        else
            overkilled = "true";
    }

    public void setMarks(int marks,PlayerColor shooterPlayerColor){

        mPlayerMarks.put(Colors.getColorTile(shooterPlayerColor.getPascalName())+
                shooterPlayerColor.getPascalName()+Colors.ANSI_RESET,marks);
    }

    public void setDamageTakenToZero(){
        mDamageTaken = "Has not suffered Damage";
    }


    public void setAllDamageTaken(PlayerColor[] damage){
        StringBuilder shooter = new StringBuilder();

        int size = Arrays.asList(damage).size();
        if(damage[0]==null) {
            setDamageTakenToZero();
        }
        else {
            List<PlayerColor> colorPlayerThatDamage = Arrays.stream(damage)
                    .distinct()
                    .collect(Collectors.toList());
            shooter.append("First damage from ->");
            shooter.append(Colors.getColorTile(damage[0].getPascalName()));
            shooter.append(damage[0].getPascalName());
            shooter.append(Colors.ANSI_RESET);
            shooter.append("\n\t\t\t\t\t ");
            mDamageTaken = shooter.append(countDamage(colorPlayerThatDamage,size,damage))
                    .toString();
        }
    }

    public StringBuilder countDamage(List<PlayerColor> colorPlayerThatDamage,int size,PlayerColor[] damage){
        StringBuilder shooter = new StringBuilder();
        int count;
        for (PlayerColor color : colorPlayerThatDamage) {
            count = 0;
            if (color != null) {
                for (int i = 0; i < size; i++) {
                    if (damage[i] != null && color.equals(damage[i])) {
                        count += 1;
                    }
                }

            }
            if(color !=null){
                shooter.append(count);
                shooter.append("<-");
                shooter.append(Colors.getColorTile(color.getPascalName()));
                shooter.append(color.getPascalName());
                shooter.append(Colors.ANSI_RESET);
                shooter.append(" ");
            }
        }
        return shooter;
    }

    public void setDamageTaken(int damageTaken, PlayerColor shooterPlayerColor){
        StringBuilder damage = new StringBuilder();
        damage.append(mDamageTaken);
        for(int i=0 ; i<damageTaken; i++)
            damage.append(shooterPlayerColor.getPascalName());
        mDamageTaken = damage.toString();
    }

    public void setWeaponOtherPlayer(Weapon[] weapons){
        List<String> actualWeapons = new ArrayList<>();
        if (weapons == null){
            actualWeapons.add(NOWEAPON);
            mPlayerWeapons = actualWeapons;
        }
        else{
            for(Weapon weapon: weapons){
                if(weapon!=null && !weapon.isLoaded()){
                        actualWeapons.add(weapon.getName());
                }
            }
        }
        mPlayerWeapons = actualWeapons;
    }

    public void setWeaponOwner(Weapon[] weapons){
        String load = "load";
        List<String> actualWeapons = new ArrayList<>();
        Map<String,String> info = new HashMap<>();
        int isLoad=1;//is load
        if (weapons == null){
            actualWeapons.add(NOWEAPON);
            mPlayerWeapons = actualWeapons;
        }
        else{

            for(Weapon weapon: weapons){
                if(weapon!= null){
                    if(!weapon.isLoaded()){
                        load = " to load cost: "+ Colors.getColorTile("red") + "Red " + Colors.ANSI_RESET + weapon.getReloadCost().getRed()+" "+
                                                  Colors.getColorTile("yellow") + "Yellow " +Colors.ANSI_RESET+ weapon.getReloadCost().getYellow()+" "+
                                                  Colors.getColorTile("blue") + "Blue "+Colors.ANSI_RESET + weapon.getReloadCost().getBlue();
                    }

                    actualWeapons.add(weapon.getName());
                    info.put(weapon.getName(),load);
                    load ="load";
                }
            }
        }

        mPlayerWeapons = actualWeapons;
        weaponsInfo = info;
    }


    public void setPosition(Position pos){
        if (pos==null)
            mPlayerPos = "not respawned";
        else {
            mPlayerPos =  pos.toString();
        }
    }

}
