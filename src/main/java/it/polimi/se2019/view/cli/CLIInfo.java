package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.List;

public class CLIInfo {
    private List<Player> mPlayers;
    private String mActivePlayerf;
    private Player mOwner;
    private PlayerColor mActivePlayer;
    private PlayerColor mOwnerColor;
    private String mOwnerColorf;


    private final static int sizePlayer = 11;
    private CLIPlayer mPlayer ;
    private List<CLIPlayer> mPlayersInfo;

    public CLIInfo (List<Player> players, Player owner,PlayerColor ownerColor,PlayerColor activePlayer){
        mPlayers = players;

        mOwner = owner;


    }

    public CLIInfo(){

    }

    public void initialization(List<Player> players){
        for (Player player : players) {
            mPlayer = new CLIPlayer(players);
        }
        mPlayersInfo.add(mPlayer);
    }
    public void setActivePlayer(PlayerColor color){
        mActivePlayerf = color.getPascalName();
    }





    public List<Player> getPlayers(){return mPlayers;}
    public Player getPlayerFromColor(PlayerColor color){
        Player target = null;
            for(Player player: mPlayers)
                if(player.getColor()== color)
                    target = player;
         return target;
    }

    public PlayerColor getActivePlayer(){return mActivePlayer;}
    public Player getOwner(){return mOwner;}
    public PlayerColor getOwnerColor(){return mOwnerColor;}
}
