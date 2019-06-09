package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.PowerUpCard;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.SpawnTile;
import it.polimi.se2019.model.board.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIInfo {

    private String mActivePlayer;
    private PlayerColor mOwnerColorf;
    private String mOwnerColor;
    private CLIPlayer mOwner;
    private List<CLIPlayer> mPlayersInfo = new ArrayList<>();
    private String mKillTrack;
    private Map<String,String> spawnTiles = new HashMap<>();
    // to add ammotile
    private BoardCLI board;

    public CLIInfo (List<Player> players, Player owner,PlayerColor ownerColor,PlayerColor activePlayer){
        initialization(players, owner,ownerColor, activePlayer);
        mOwnerColorf=ownerColor;
    }

    public CLIInfo (List<Player> players, Player owner, PlayerColor ownerColor, PlayerColor activePlayer, Board board){
        initialization(players, owner,ownerColor, activePlayer);
        mOwnerColorf=ownerColor;

        for(Tile tile :board.getTiles()){
            if(tile.getTileType().equalsIgnoreCase("spawn")){
                SpawnTile spawn = (SpawnTile)tile;
                spawnTiles.put(tile.getColor().getPascalName(),weaponToSting(spawn.getWeapons()));
            }
        }

    }


    public void initialization(List<Player> players,Player owner,PlayerColor ownerColor,PlayerColor activePlayerColor){
        CLIPlayer playerInfo;
        setActivePlayer(activePlayerColor);
        mOwnerColor = ownerColor.getPascalName();
        for (Player player : players) {
            if(player!=null){
                playerInfo = new CLIPlayer(player);
                if(player.getColor().getPascalName().equals(mOwnerColor)){
                   playerInfo.setWeaponOwner(player.getWeapons());
                   playerInfo.setPowerUpsOwnerPlayer(player.getPowerUps());
                   mOwner = playerInfo;
                }
                mPlayersInfo.add(playerInfo);
            }
        }

    }

    public void setActivePlayer(PlayerColor playerColor){
        mActivePlayer = playerColor.getPascalName();
    }

    public void updatePowerUps(PlayerColor playerColor, PowerUpCard[] powerUpCards){
        if(playerColor.getPascalName().equals(mOwnerColor)){
            mOwner.setPowerUpsOwnerPlayer(powerUpCards);
            return;
        }
        if(powerUpCards == null)
            playerFromColor(playerColor).setPowerUpsOtherPlayers(0);
        else
            playerFromColor(playerColor).setPowerUpsOtherPlayers(powerUpCards.length);
    }


    public void updateMarks(PlayerColor targetColor, int marks, PlayerColor shooterColor){
        playerFromColor(targetColor).setMarks(marks,shooterColor);
    }


    public void updateWeapon(PlayerColor playerColor, Weapon[] weapons){
        if(playerColor.getPascalName().equals(mOwnerColor)) {
            mOwner.setWeaponOwner(weapons);
            return;
        }
        playerFromColor(playerColor).setWeaponOtherPlayer(weapons);
    }

    public void updatePosition(PlayerColor playerColor, Position pos){
        playerFromColor(playerColor).setPosition(pos);
    }

    public void updateDamage(PlayerColor damagedPlayerColor,int damageTaken,PlayerColor shooterPlayerColor){
        playerFromColor(damagedPlayerColor).setDamageTaken(damageTaken,shooterPlayerColor);
    }

 //   public void updateBoardFlip(PlayerColor playerColor){
 //        playerFromColor(playerColor).flipBoard(playerFromColor(playerColor).;);
//    }

    public void updateOnRespan(PlayerColor playerColor){
        playerFromColor(playerColor).setOverkilled(false);
        playerFromColor(playerColor).setDead(false);
        playerFromColor(playerColor).setDamageTakenToZero();
    }

    public void updateKillTrak(PlayerColor killedColor,PlayerColor killerColor,
                                boolean overkill, Map<PlayerColor,Integer> scores){
        StringBuilder killTrack = new StringBuilder();
        killTrack.append(mKillTrack);
        playerFromColor(killedColor).setDeathNums();
        playerFromColor(killedColor).setDead(true);
        for(Map.Entry<PlayerColor,Integer> entry: scores.entrySet()){
            playerFromColor(entry.getKey()).setScore(scores.get(entry.getKey()));
        }

        killTrack.append(playerFromColor(killerColor).getPlayerName());
        if(overkill){
            playerFromColor(killedColor).setOverkilled(true);
            killTrack.append(" with overkill");
        }
        killTrack.append("\n");
        mKillTrack=killTrack.toString();
    }

    public CLIPlayer playerFromColor(PlayerColor color) {
        CLIPlayer target = null;
        for (CLIPlayer player : mPlayersInfo) {
        if (player.getPlayerColor().equals(color.getPascalName()))
            target = player;
        }
        return target;
    }

    public String weaponToSting(Weapon[] weapons){
        StringBuilder stringWeapons = new StringBuilder();
        for (Weapon weapon : weapons) {
                stringWeapons.append(weapon.getName());
                stringWeapons.append("  Cost: ");
                stringWeapons.append(weapon.getGrabCost().toString());
        }
        return stringWeapons.toString();
    }

      public String getActivePlayer(){return mActivePlayer;}
      public Map<String,String> getSpawnTiles(){return spawnTiles;}
      public CLIPlayer getOwner(){return mOwner;}
      public String getOwnerColor(){return mOwnerColor;}
      public PlayerColor getOwnerColorf(){return mOwnerColorf;}
      public List<CLIPlayer> getPlayers(){return mPlayersInfo;}
}
