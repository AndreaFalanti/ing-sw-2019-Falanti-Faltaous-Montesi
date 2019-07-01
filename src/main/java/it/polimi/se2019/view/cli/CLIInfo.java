package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.se2019.view.cli.Colors.ANSI_RESET;
import static it.polimi.se2019.view.cli.Colors.findColor;

public class CLIInfo {

    private static final int BOARD_COLUMNS =4;
    private static final int BOARD_ROWS=3;
    private String mActivePlayer;
    private PlayerColor mOwnerColorf;
    private String mOwnerColor;
    private CLIPlayer mOwner;
    private List<PlayerColor> mKills;
    private List<PlayerColor> mOverkills;
    private EnumMap<PlayerColor,CLIPlayer> mPlayersInfo = new EnumMap<>(PlayerColor.class);
    private String mKillTrack;
    private EnumMap<TileColor,String> spawnTiles = new EnumMap<>(TileColor.class);
    private Map<Position,String> normalTiles = new HashMap<>();
    private Map<TileColor,String> tilesColor = new EnumMap<>(TileColor.class);
    private BoardCLI mBoard ;
    private int mTurn;


    public String                       getActivePlayer(){return mActivePlayer;}
    public Map<TileColor,String>        getSpawnTiles(){return spawnTiles;}
    public CLIPlayer                    getOwner(){return mOwner;}
    public PlayerColor                  getOwnerColorf(){return mOwnerColorf;}
    public Map<PlayerColor, CLIPlayer>  getPlayersInfo() { return mPlayersInfo; }
    public Map<Position, String>        getNormalTiles(){ return normalTiles; }
    public BoardCLI                     getBoard(){ return mBoard; }
    public Map<TileColor,String>        getTilesColor(){return tilesColor;}

    public CLIInfo (List<Player> players, PlayerColor ownerColor, PlayerColor activePlayer, Board board,int turn,List<PlayerColor> kills, List<PlayerColor> overkills){
        Tile tile;
        initialization(players,ownerColor, activePlayer);
        mOwnerColorf=ownerColor;
        mTurn = turn;
        mKills = kills;
        mOverkills = overkills;
        mBoard = new BoardCLI(board);
        for (int x = 0; x < BOARD_COLUMNS; x++) {
            for (int y = 0; y < BOARD_ROWS; y++) {
                if (board.getTileAt(new Position(x, y)) != null) {
                    tile=board.getTileAt(new Position(x, y));
                    tilesColor.put(tile.getColor(),tile.getColor().getPascalName());
                    if(tile.getTileType().equalsIgnoreCase("spawn")){
                        setSpawnTiles(tile);
                    } else{
                        setNormalTiles(tile,new Position(x,y));
                    }
                }
            }
        }
    }

    public void initialization(List<Player> players,PlayerColor ownerColor,PlayerColor activePlayerColor){
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
        if(spawn.getWeapons()!=null)
            spawnTiles.put(tile.getColor(),weaponToSting(spawn.getWeapons()));
        else
            spawnTiles.put(tile.getColor(),"nothing");
    }

    public void setNormalTiles(Tile tile,Position pos){
        NormalTile normal = (NormalTile)tile;
        if(normal.getAmmoCard()!=null)
            normalTiles.put(pos,normal.getAmmoCard().toString());
        else
            normalTiles.put(pos,"nothing");
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
                stringWeapons.append(cost(weapon.getGrabCost().getRed(),"red"));
                stringWeapons.append(cost(weapon.getGrabCost().getYellow(),"yellow"));
                stringWeapons.append(cost(weapon.getGrabCost().getBlue(),"blue"));
                stringWeapons.append(";");
            }
        }

        return stringWeapons.toString();
    }

    public String cost(int value,String color){
        StringBuilder cost = new StringBuilder();
        cost.append(findColor(color));
        if(color.equalsIgnoreCase("red"))
            cost.append("Red : ");
        if(color.equalsIgnoreCase("yellow"))
            cost.append("Yellow : ");
        if(color.equalsIgnoreCase("blue"))
            cost.append("Blue : ");
        cost.append(value+" ");
        cost.append(ANSI_RESET);
        return cost.toString();
    }


}
