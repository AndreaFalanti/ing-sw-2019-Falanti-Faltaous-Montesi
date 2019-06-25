package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CLIInfo {

    private static int BOARD_COLUMNS =4;
    private static int BOARD_ROWS=3;
    private String mActivePlayer;
    private PlayerColor mOwnerColorf;
    private String mOwnerColor;
    private CLIPlayer mOwner;
    private EnumMap<PlayerColor,CLIPlayer> mPlayersInfo = new EnumMap<>(PlayerColor.class);
    private String mKillTrack;
    private EnumMap<TileColor,String> spawnTiles = new EnumMap<>(TileColor.class);
    private Map<Position,String> normalTiles = new HashMap<>();
    private BoardCLI mBoard ;


    public CLIInfo (List<Player> players, Player owner,PlayerColor ownerColor,PlayerColor activePlayer){
        initialization(players, owner,ownerColor, activePlayer);
        mOwnerColorf=ownerColor;
    }

    public CLIInfo (List<Player> players, Player owner, PlayerColor ownerColor, PlayerColor activePlayer, Board board){
        Tile tile;
        initialization(players, owner,ownerColor, activePlayer);
        mOwnerColorf=ownerColor;

        mBoard = new BoardCLI(board);
        for (int x = 0; x < BOARD_COLUMNS; x++) {
            for (int y = 0; y < BOARD_ROWS; y++) {
                if (board.getTileAt(new Position(x, y)) != null) {
                    tile=board.getTileAt(new Position(x, y));
                    if(tile.getTileType().equalsIgnoreCase("spawn")){
                        SpawnTile spawn = (SpawnTile)tile;
                        if(spawn.getWeapons()!=null)
                            spawnTiles.put(tile.getColor(),weaponToSting(spawn.getWeapons()));
                        else
                            spawnTiles.put(tile.getColor(),"nothing");
                    } else{

                            NormalTile normal = (NormalTile)tile;
                            if(normal.getAmmoCard()!=null)
                                normalTiles.put((new Position(x,y)),normal.getAmmoCard().toString());
                            else
                                normalTiles.put((new Position(x,y)),"nothing");
                    }
                }
            }
        }
    }

    public void initialization(List<Player> players,Player owner,PlayerColor ownerColor,PlayerColor activePlayerColor){
        CLIPlayer playerInfo;
        setActivePlayer(activePlayerColor);
        mOwnerColor = ownerColor.getPascalName();
        for (Player player : players) {
            if(player!=null){
                playerInfo = new CLIPlayer(player,ownerColor);
                if(player.getColor().getPascalName().equals(mOwnerColor)){
                   mOwner = playerInfo;
                }
                mPlayersInfo.put(player.getColor(),playerInfo);
            }
        }

    }

    public void setActivePlayer(PlayerColor playerColor){
        mActivePlayer = playerColor.getPascalName();
    }

    public void setSpawnTiles(Tile tile){
        SpawnTile spawn = (SpawnTile)tile;
        spawnTiles.put(spawn.getColor(),weaponToSting(spawn.getWeapons()));
    }

    public void setNormalTiles(Tile tile,Position pos){
        NormalTile normal = (NormalTile)tile;
        normalTiles.put(pos,normal.getAmmoCard().toString());
    }

    public void updatePowerUps(PlayerColor playerColor, PowerUpCard[] powerUpCards){
        if(playerColor.getPascalName().equals(mOwnerColor)){
            mOwner.setPowerUpsOwnerPlayer(powerUpCards);
            return;
        }
        if(powerUpCards == null)
            mPlayersInfo.get(playerColor).setPowerUpsOtherPlayers(0);
        else
            mPlayersInfo.get(playerColor).setPowerUpsOtherPlayers(powerUpCards.length);
    }

    public void updateMarks(PlayerColor targetColor, int marks, PlayerColor shooterColor){
        mPlayersInfo.get(targetColor).setMarks(marks,shooterColor);
    }

    public void updateWeapon(PlayerColor playerColor, Weapon[] weapons){
        if(playerColor.getPascalName().equals(mOwnerColor)) {
            mOwner.setWeaponOwner(weapons);
            return;
        }
        mPlayersInfo.get(playerColor).setWeaponOtherPlayer(weapons);
    }

    public void updatePosition(PlayerColor playerColor, Position pos){
        mPlayersInfo.get(playerColor).setPosition(pos);
    }

    public void updateDamage(PlayerColor damagedPlayerColor,int damageTaken,PlayerColor shooterPlayerColor){
        mPlayersInfo.get(damagedPlayerColor).setDamageTaken(damageTaken,shooterPlayerColor);
    }

    public void updateBoardFlip(PlayerColor playerColor){
        mPlayersInfo.get(playerColor).setBoardFlipped();
    }

    public void updateAmmo(PlayerColor playerColor, AmmoValue ammo){
        mPlayersInfo.get(playerColor).setAmmo(ammo);
    }

    public void updateOnRespawn(PlayerColor playerColor){
        mPlayersInfo.get(playerColor).setOverkilled(false);
        mPlayersInfo.get(playerColor).setDead(false);
        mPlayersInfo.get(playerColor).setDamageTakenToZero();
    }

    public void updateKillTrak(PlayerColor killedColor,PlayerColor killerColor,
                                boolean overkill, Map<PlayerColor,Integer> scores){
        StringBuilder killTrack = new StringBuilder();
        killTrack.append(mKillTrack);
        mPlayersInfo.get(killedColor).setDeathNums();
        mPlayersInfo.get(killedColor).setDead(true);
        for(Map.Entry<PlayerColor,Integer> entry: scores.entrySet()){
            mPlayersInfo.get(entry.getKey()).setScore(scores.get(entry.getKey()));
        }

        killTrack.append(mPlayersInfo.get(killerColor).getPlayerName());
        if(overkill){
            mPlayersInfo.get(killedColor).setOverkilled(true);
            killTrack.append(" with overkill");
        }
        killTrack.append("\n");
        mKillTrack=killTrack.toString();
    }


    public PlayerColor colorFromName(String name) {
        PlayerColor target = null;
        for (Map.Entry<PlayerColor,CLIPlayer> player: mPlayersInfo.entrySet()) {
            if (player.getValue().getPlayerName().equalsIgnoreCase(name))
                target = player.getKey();
        }
        return target;
    }

    public String weaponToSting(Weapon[] weapons){
        StringBuilder stringWeapons = new StringBuilder();
        for (Weapon weapon : weapons) {
            if(weapon!=null){
                stringWeapons.append(weapon.getName());
                stringWeapons.append("  Cost: ");
                stringWeapons.append(weapon.getGrabCost().toString());
            }
        }
        return stringWeapons.toString();
    }

    public String getActivePlayer(){return mActivePlayer;}
    public Map<TileColor,String> getSpawnTiles(){return spawnTiles;}
    public CLIPlayer getOwner(){return mOwner;}
    public String getOwnerColor(){return mOwnerColor;}
    public PlayerColor getOwnerColorf(){return mOwnerColorf;}

    public Map<PlayerColor, CLIPlayer> getPlayersInfo() {
        return mPlayersInfo;
    }
    public Map<Position, String> getNormalTiles(){
        return normalTiles;
    }
    public BoardCLI getBoard(){
        return mBoard;
    }
}
