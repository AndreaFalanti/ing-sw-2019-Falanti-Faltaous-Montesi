package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.List;

public class CLIInfo {
    private List<Player> mPlayers;
    private PlayerColor mActivePlayer;
    private Player mOwner;
    private PlayerColor mOwnerColor;
    public CLIInfo (List<Player> players, Player owner,PlayerColor ownerColor,PlayerColor activePlayer){
        mPlayers = players;
        mOwner = owner;
        mOwnerColor = ownerColor;
        mActivePlayer = activePlayer;
    }

    public List<Player> getPlayers(){return mPlayers;}
    public PlayerColor getActivePlayer(){return mActivePlayer;}
    public Player getOwner(){return mOwner;}
    public PlayerColor getOwnerColor(){return mOwnerColor;}
}
