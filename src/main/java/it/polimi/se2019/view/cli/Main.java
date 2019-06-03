package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        List<Player> mPlayers = new ArrayList<>() ;
        PlayerColor activePlayer = null;
        Player owner = null;
        PlayerColor ownerColor = null;

        CLIInfo cLIInfo = new CLIInfo(mPlayers,owner,ownerColor,activePlayer);
        CLIView cliView = new CLIView(cLIInfo);
        cliView.availableCommands();
    }
}