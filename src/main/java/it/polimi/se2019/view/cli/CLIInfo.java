package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class CLIInfo {
    private List<Player> mPlayers;
    private PlayerColor mActivePlayer;
    private Player mOwner;
    private PlayerColor mOwnerColor;

    private final static int sizePlayer = 11;
    private CLIPlayer mPlayer ;
    private List<String[]> mPlayersInfo;

    public CLIInfo (List<Player> players, Player owner,PlayerColor ownerColor,PlayerColor activePlayer){
        mPlayers = players;

        mOwner = owner;
        mOwnerColor = ownerColor;
        mActivePlayer = activePlayer;
    }

    public CLIInfo(){

    }

    public void setActivePlayer(PlayerColor color){
        mActivePlayerf = color.getPascalName();
    }

    public void initializationAllPlayers(List<Player> players){
        for(Player player: players) {
            mPlayer[mPlayerName] = player.getName();
            mPlayer[mPlayerColor] = player.getColor().getPascalName();
            mPlayer[mPlayerScore] = String.valueOf(player.getScore());

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
        for(PlayerColor color: damagedPlayer)


    }



    public List<Player> getPlayers(){return mPlayers;}
    public Player getPlayerFromColor(PlayerColor color){
        Player target = null;
            for(Player player: mPlayers)
                if(player.getColor()== color)
                    target = player;
         return target;
    }
    public String getDamageTaken(){return mDamageTaken};
    public PlayerColor getActivePlayer(){return mActivePlayer;}
    public Player getOwner(){return mOwner;}
    public PlayerColor getOwnerColor(){return mOwnerColor;}
}
