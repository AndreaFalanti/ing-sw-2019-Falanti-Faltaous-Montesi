package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.*;

import java.util.*;

import static it.polimi.se2019.view.cli.Colors.ANSI_RESET;
import static it.polimi.se2019.view.cli.Colors.findColor;

public class CLIInfo {

    private static final int BOARD_COLUMNS =4;
    private static final int BOARD_ROWS=3;
    private PlayerColor mActivePlayer;
    private PlayerColor mOwnerColor;
    private CLIPlayer mOwner;
    private List<String> mKills = new ArrayList<>();
    private List<String> mOverkills = new ArrayList<>();
    private EnumMap<PlayerColor,CLIPlayer> mPlayersInfo = new EnumMap<>(PlayerColor.class);
    private EnumMap<TileColor,String> spawnTiles = new EnumMap<>(TileColor.class);
    private Map<Position,String> normalTiles = new HashMap<>();
    private Map<TileColor,String> tilesColor = new EnumMap<>(TileColor.class);
    private BoardCLI mBoard ;
    private int mTurn;


    public PlayerColor                  getActivePlayer(){return mActivePlayer;}
    public Map<TileColor,String>        getSpawnTiles(){return spawnTiles;}
    public CLIPlayer                    getOwner(){return mOwner;}
    public PlayerColor                  getOwnerColor(){return mOwnerColor;}
    public Map<PlayerColor, CLIPlayer>  getPlayersInfo() { return mPlayersInfo; }
    public Map<Position, String>        getNormalTiles(){ return normalTiles; }
    public BoardCLI                     getBoard(){ return mBoard; }
    public List<String>                 getKills(){ return mKills;}
    public List<String>                 getOverkills(){ return mOverkills;}
    public int                          getTurn(){ return mTurn;}
    public Map<TileColor,String>        getTilesColor(){return tilesColor;}

    public CLIInfo (List<Player> players, PlayerColor ownerColor, PlayerColor activePlayer, Board board,int turn,List<PlayerColor> kills, List<PlayerColor> overkills){
        Tile tile;
        initialization(players,ownerColor, activePlayer);
        mOwnerColor = ownerColor;
        mTurn = turn;
        setKillTrack(kills,overkills);
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
        mOwnerColor = ownerColor;
        for (Player player : players) {
            if(player!=null){
                playerInfo = new CLIPlayer(player,ownerColor);
                if(player.getColor().getPascalName().equals(mOwnerColor.getPascalName())){
                    mOwner = playerInfo;
                }
                mPlayersInfo.put(player.getColor(),playerInfo);
            }
        }

    }

    public void setActivePlayer(PlayerColor playerColor){
        mActivePlayer = playerColor;
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
            normalTiles.put(pos,stringAmmoCard(normal.getAmmoCard()));
        else
            normalTiles.put(pos,"nothing");
    }


    public String stringAmmoCard(AmmoCard ammoCard){


        return          " PowerUp : " +
                        ammoCard.getDrawPowerUp()+
                        " Ammo : "+
                        colorAmmo(ammoCard.getAmmoGain().getRed(),"red")+
                        colorAmmo(ammoCard.getAmmoGain().getYellow(),"yellow")+
                        colorAmmo(ammoCard.getAmmoGain().getBlue(),"blue");


    }

    public void updatePowerUps(PlayerColor playerColor, PowerUpCard[] powerUpCards){
        if(playerColor.getPascalName().equals(mOwnerColor.getPascalName())){
            mOwner.setPowerUpsOwnerPlayer(powerUpCards);
            return;
        }
        if(powerUpCards == null)
            mPlayersInfo.get(playerColor).setPowerUpsOtherPlayers(0);
        else
            mPlayersInfo.get(playerColor).setPowerUpsOtherPlayers(Arrays.asList(powerUpCards).size());
    }

    public void updateMarks(PlayerColor targetColor, int marks, PlayerColor shooterColor){
        mPlayersInfo.get(targetColor).setMarks(marks,shooterColor);
    }

    public void updateWeapon(PlayerColor playerColor, Weapon[] weapons){
        if(playerColor.getPascalName().equals(mOwnerColor.getPascalName())) {
            mOwner.setWeaponOwner(weapons);
            return;
        }
        mPlayersInfo.get(playerColor).setWeaponOtherPlayer(weapons);
    }

    public void updatePosition(PlayerColor playerColor, Position pos){


        mPlayersInfo.get(playerColor).setPosition(pos);
    }

    public void updateDamage(PlayerColor damagedPlayerColor,PlayerColor[] damageTaken){
        mPlayersInfo.get(damagedPlayerColor).setAllDamageTaken(damageTaken);
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

    public void setKillTrack(List<PlayerColor> kills, List<PlayerColor> overkills){

        for (PlayerColor kill : kills) {
            mKills.add(kill.getPascalName());
        }
        for(PlayerColor overkill: overkills){
            mOverkills.add(overkill.getPascalName());
        }
    }

    public void updateKillTrack(PlayerColor killedColor,PlayerColor killerColor,
                                boolean overkill, Map<PlayerColor,Integer> scores){
        mPlayersInfo.get(killedColor).setDeathNums();
        mPlayersInfo.get(killedColor).setDead(true);
        for(Map.Entry<PlayerColor,Integer> entry: scores.entrySet()){
            mPlayersInfo.get(entry.getKey()).setScore(scores.get(entry.getKey()));
        }

        mKills.add(killerColor.getPascalName());
        if(overkill){
            mPlayersInfo.get(killedColor).setOverkilled(true);
            mOverkills.add(killerColor.getPascalName());
        }
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
                stringWeapons.append(colorAmmo(weapon.getGrabCost().getRed(),"red"));
                stringWeapons.append(colorAmmo(weapon.getGrabCost().getYellow(),"yellow"));
                stringWeapons.append(colorAmmo(weapon.getGrabCost().getBlue(),"blue"));
                stringWeapons.append(";");
            }
        }

        return stringWeapons.toString();
    }

    public String colorAmmo(int value,String color){
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