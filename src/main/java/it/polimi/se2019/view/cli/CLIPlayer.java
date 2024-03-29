
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
    private List<String> mDamageTaken = new ArrayList<>();
    private List<String> mPlayerPowerUps = new ArrayList<>();
    private int mDeathNum;
    private int mValueOfDamage;
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
        mDeathNum = player.getDeathsNum();
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

    public List<String> getPlayerPowerUps(){return mPlayerPowerUps;}

    public String getPlayerAmmo(){return mPlayerAmmo;}

    public int getPlayerDeaths(){return mDeathNum;}

    public List<String> getPlayerDamage(){return mDamageTaken;}

    public Map<String, Integer> getPlayerMarks(){return mPlayerMarks;}

    public String playerIsDead(){return dead;}

    public String playerIsOverkilled(){return overkilled;}

    public String getPlayerPosition(){return mPlayerPos;}

    public List<String> getPlayerWeapons(){return mPlayerWeapons;}

    public Map<String, String> getWeaponsInfo(){return weaponsInfo;}

    public String getPlayerScore(){return mPlayerScore;}

    public int getValueOfDamage(){return mValueOfDamage;}

    public String getBoardFlipped(){return mBoardFlipped;}


    public void setScore(int i){
        mPlayerScore = String.valueOf(i);
    }

    public void setDeathNums(){
        mDeathNum = mDeathNum + 1;
    }

    public void setBoardFlipped(boolean flipped){
        if(flipped)
            mBoardFlipped = "true";
        else
            mBoardFlipped = "false";
    }

    public void setBoardFlipped(){
        mBoardFlipped = "true";
    }

    public void setAmmo(AmmoValue ammo){

        mPlayerAmmo =   Colors.findColor("red") + "Red " + Colors.ANSI_RESET + ammo.getRed()+" "+
                Colors.findColor("yellow") + "Yellow " +Colors.ANSI_RESET+ ammo.getYellow()+" "+
                Colors.findColor("blue") + "Blue "+Colors.ANSI_RESET + ammo.getBlue();
    }

    public void setPowerUpsOtherPlayers(int i){
        List<String> powerUps = new ArrayList<>();
        powerUps.add(String.valueOf(i));
        mPlayerPowerUps = powerUps;
    }

    public void setPowerUpsOwnerPlayer(PowerUpCard[] powerUpCards){
        List<String> powerUps = new ArrayList<>();

        int i=0;
        if (powerUpCards == null){
            powerUps.add("not have power up card !");
            mPlayerPowerUps = powerUps;
            return;
        }

        for (PowerUpCard powerUpCard : powerUpCards) {
            StringBuilder power = new StringBuilder();
            if(powerUpCard != null){
                power.append(i);
                power.append(")Name : ");
                power.append(powerUpCard.getType());
                power.append(" ");
                power.append("Color: ");
                power.append(Colors.findColor(powerUpCard.getColor().getPascalName()));
                power.append(powerUpCard.getColor().getPascalName());
                power.append(Colors.ANSI_RESET);
                power.append(SPACE+" ");
            }
            else{
                power.append(i);
                power.append(")Not a card");
                power.append(SPACE+" ");
            }
              i++;
            powerUps.add(power.toString());
        }
        mPlayerPowerUps = powerUps;
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

        mPlayerMarks.put(Colors.findColor(shooterPlayerColor.getPascalName())+
                shooterPlayerColor.getPascalName()+Colors.ANSI_RESET,marks);
    }

    public void setDamageTakenToZero(){
        List<String> noDamage = new ArrayList<>();
        noDamage.add("Has not suffered Damage");
        mDamageTaken = noDamage;
    }


    public void setAllDamageTaken(PlayerColor[] damage){
        StringBuilder shooter = new StringBuilder();
        List<String> damageInfo = new ArrayList<>();
        int size = Arrays.asList(damage).size();
        if(damage[0]==null) {
            setDamageTakenToZero();
        }
        else {
            List<PlayerColor> colorPlayerThatDamage = Arrays.stream(damage)
                    .distinct()
                    .collect(Collectors.toList());
            shooter.append("First damage from ->");
            shooter.append(Colors.findColor(damage[0].getPascalName()));
            shooter.append(damage[0].getPascalName());
            shooter.append(Colors.ANSI_RESET);
            shooter.append("\n\t\t\t\t\t ");
            damageInfo.add(shooter.toString());
            damageInfo.addAll(countDamage(colorPlayerThatDamage,size,damage));
            mDamageTaken = damageInfo;
        }

    }

    public List<String> countDamage(List<PlayerColor> colorPlayerThatDamage,int size,PlayerColor[] damage){
        List<String> damageInfo = new ArrayList<>();
        int count;
        int sumDamage = 0;
        for (PlayerColor color : colorPlayerThatDamage) {
            StringBuilder shooter = new StringBuilder();
            count = 0;
            if (color != null) {
                for (int i = 0; i < size; i++) {
                    if (damage[i] != null && color.equals(damage[i])) {
                        count += 1;
                    }
                }

            }
            if(color !=null){
                shooter.append(createSingleStringDamage(count,color));
            }
            damageInfo.add(shooter.toString());
            sumDamage += count;
        }
        mValueOfDamage = sumDamage;
        return damageInfo;
    }

    public StringBuilder createSingleStringDamage(int count,PlayerColor color){
        StringBuilder shooter = new StringBuilder();

        shooter.append(count);
        shooter.append("<-");
        shooter.append(Colors.findColor(color.getPascalName()));
        shooter.append(color.getPascalName());
        shooter.append(Colors.ANSI_RESET);
        shooter.append(" ");

        return shooter;
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
                        load = " to load cost: "+ Colors.findColor("red") + "Red " + Colors.ANSI_RESET + weapon.getReloadCost().getRed()+" "+
                                Colors.findColor("yellow") + "Yellow " +Colors.ANSI_RESET+ weapon.getReloadCost().getYellow()+" "+
                                Colors.findColor("blue") + "Blue "+Colors.ANSI_RESET + weapon.getReloadCost().getBlue()+", ";
                    }

                    actualWeapons.add(weapon.getName());
                    info.put(weapon.getName(),load);
                    load ="load, ";
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


