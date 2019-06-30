package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Pair;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.ShootRequest;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {


    public static void main2(String[] args) throws IOException, NotBoundException {
        // test



        //end test
        LoginCLI.log();
      //  CLIInfo cLIInfo = new CLIInfo(mPlayers,ownerColor,activePlayer,board);

        CLIView cliView = new CLIView(null);
        cliView.availableCommands();
    }

    public static void main(String[] args) {
        AmmoValue initialAmmo = new AmmoValue(3, 3, 3);
        Game game = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.PURPLE, new Position(3, 2), initialAmmo),
                        new Player("Luigi", PlayerColor.GREEN, new Position(2, 0), initialAmmo),
                        new Player("Dorian", PlayerColor.GREY, new Position(3,2 ), initialAmmo),
                        new Player("Smurfette", PlayerColor.BLUE, new Position(2, 2), initialAmmo),
                        new Player("Stones", PlayerColor.YELLOW, new Position(2, 1), initialAmmo)
                )),
                1
        );

        Map<PlayerColor, View> viewMap = Stream.of(
                new Pair<>(
                        PlayerColor.PURPLE,
                        new CLIView(
                            game.extractViewInitializationInfo(),PlayerColor.PURPLE
                        )
                ),
                new Pair<>(
                        PlayerColor.BLUE,
                        new CLIView(
                                game.extractViewInitializationInfo(),PlayerColor.BLUE
                        )
                ),
                new Pair<>(
                        PlayerColor.YELLOW,
                        new CLIView(
                                game.extractViewInitializationInfo(),PlayerColor.YELLOW
                        )
                )
        )
                .collect(Collectors.toMap(
                        p -> p.getFirst(),
                        p -> p.getSecond()
                ));

        Controller controller = new Controller(game, viewMap);
        game.register(viewMap.get(PlayerColor.YELLOW));
        viewMap.get(PlayerColor.YELLOW).register(controller);
        viewMap.get(PlayerColor.YELLOW).notify(new ShootRequest(PlayerColor.YELLOW, "plasma_gun" ,PlayerColor.YELLOW));
        System.out.println(controller.getGame().getActivePlayer().getPos());
  //      viewMap.get(PlayerColor.YELLOW).reinitialize(game.extractViewInitializationInfo().setOwnerColor(PlayerColor.YELLOW));
        CLIView cli = (CLIView)viewMap.get(PlayerColor.YELLOW);
        cli.availableCommands();
      //  System.out.println(controller.getGame().getPlayerFromColor(PlayerColor.BLUE).getDamageTaken());
    }
/*
    public static void main1(String[] args) {
        AmmoValue initialAmmo= new AmmoValue(3,3,3);
        Game game = new Game(
                Board.fromJson(Jsons.get("boards/game/board3")),
                new ArrayList<>(Arrays.asList(
                        new Player("Mario", PlayerColor.GREEN, new Position(3, 2), initialAmmo),
                        new Player("Luigi", PlayerColor.YELLOW, new Position(2, 0), initialAmmo),
                        new Player("Smurfette", PlayerColor.PURPLE, new Position(2, 0), initialAmmo)
                )),
                1
        );


        //end test
        PlayerColor ownerColor = PlayerColor.YELLOW;
        PlayerColor activePlayer = PlayerColor.YELLOW;
        new CLIView(game.extractViewInitializationInfo(),ownerColor).availableCommands();
    }*/
}