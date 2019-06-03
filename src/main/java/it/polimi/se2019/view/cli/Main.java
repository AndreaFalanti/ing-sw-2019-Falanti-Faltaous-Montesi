package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.Player;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.Position;

import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        //test
        List<Player> mPlayers = new ArrayList<>() ;
        PlayerColor activePlayer = PlayerColor.BLUE;
        Player owner = new Player("Owner",PlayerColor.GREEN,new Position(0,0));
        Player player1 = new Player("Player1",PlayerColor.BLUE,new Position(1,0));
        Player player2 = new Player("Player2",PlayerColor.GREY,new Position(2,2));
        Player player3 = new Player("Player3",PlayerColor.YELLOW,new Position(3,0));
        Player player4 = new Player("Player4",PlayerColor.PURPLE,new Position(1,3));
        mPlayers.add(owner);
        mPlayers.add(player1);
        mPlayers.add(player2);
        mPlayers.add(player3);
        mPlayers.add(player4);
        PlayerColor ownerColor = PlayerColor.GREEN;
        //end test

        CLIInfo cLIInfo = new CLIInfo(mPlayers,owner,ownerColor,activePlayer);
        CLIView cliView = new CLIView(cLIInfo);
        cliView.availableCommands();
    }
}