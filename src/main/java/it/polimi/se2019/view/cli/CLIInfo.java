package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.List;

public class CLIInfo {
    private List<Player> mPlayers;
    private String mActivePlayer;
    private Player mOwner;
    private String mOwnerColor;


    private final static int sizePlayer = 11;
    private CLIPlayer mPlayer ;
    private List<String[]> mPlayersInfo;

    public CLIInfo (List<Player> players, Player owner,PlayerColor ownerColor,PlayerColor activePlayer){
        mPlayers = players;

        mOwner = owner;


    }

    public CLIInfo(){

    }

    public void initialization(List<Player> players){
        mPlayer = new CLIPlayer(players);
    }
    public void setActivePlayer(PlayerColor color){
        mActivePlayer = color.getPascalName();
    }




    public List<Player> getPlayers(){return mPlayers;}
    public Player getPlayerFromColor(PlayerColor color){
        Player target = null;
            for(Player player: mPlayers)
                if(player.getColor()== color)
                    target = player;
         return target;
    }

    public String getActivePlayer(){return mActivePlayer;}
    public Player getOwner(){return mOwner;}
    public String getOwnerColor(){return mOwnerColor;}
}
