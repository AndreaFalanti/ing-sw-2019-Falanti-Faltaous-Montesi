package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.controller.weapon.Weapons;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Pair;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.ShootRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {


    public static void main(String[] args) {
        //test
        List<Player> mPlayers = new ArrayList<>() ;
        PlayerColor activePlayer = PlayerColor.BLUE;
        Player owner = new Player("Owner",PlayerColor.GREEN,new Position(3,1));
        Player player1 = new Player("Player1",PlayerColor.BLUE,new Position(1,2));
        Player player2 = new Player("Player2",PlayerColor.GREY,new Position(1,2));
        Player player3 = new Player("Player3",PlayerColor.YELLOW,new Position(1,1));
        Player player4 = new Player("Player4",PlayerColor.PURPLE,new Position(2,2));
        Weapon weapon1 = Weapons.get("heatseeker");
        Weapon weapon2 = Weapons.get("hellion");
        Weapon weapon3 = Weapons.get("lock_rifle");
        weapon2.setLoaded(true);
        player1.addWeapon(weapon1);
        player1.addWeapon(weapon2);
        player1.addWeapon(weapon3);
        player2.addWeapon(weapon1);
        player2.addWeapon(weapon2);
        player2.addWeapon(weapon3);
        player3.addWeapon(weapon1);
        player3.addWeapon(weapon2);
        player3.addWeapon(weapon3);
        player4.addWeapon(weapon1);
        player4.addWeapon(weapon2);
        player4.addWeapon(weapon2);
        owner.addWeapon(weapon3);
        owner.addWeapon(weapon1);
        owner.addWeapon(weapon2);
        owner.onDamageTaken(new Damage(3,0), PlayerColor.YELLOW);
        owner.onDamageTaken(new Damage(3,1), PlayerColor.BLUE);
        owner.onDamageTaken(new Damage(3,0), PlayerColor.YELLOW);
        owner.onDamageTaken(new Damage(3,2), PlayerColor.GREY);
        player2.onDamageTaken(new Damage(3,0), PlayerColor.YELLOW);
        player2.onDamageTaken(new Damage(3,1), PlayerColor.YELLOW);
        player3.onDamageTaken(new Damage(3,2), PlayerColor.YELLOW);
        PowerUpCard card1 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0));
        PowerUpCard card2 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0));
        PowerUpCard card3 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0));
        owner.addPowerUp(card1);
        owner.addPowerUp(card2);
        owner.addPowerUp(card3);
        player1.addPowerUp(card1);
        player1.addPowerUp(card2);
        player2.addPowerUp(card3);
        mPlayers.add(owner);
        mPlayers.add(player1);
        mPlayers.add(player2);
        mPlayers.add(player3);
        mPlayers.add(player4);
        PlayerColor ownerColor = PlayerColor.GREEN;
        Board board = Board.fromJson(Jsons.get("boards/game/board1"));


        //end test

        CLIInfo cLIInfo = new CLIInfo(mPlayers,owner,ownerColor,activePlayer,board);
        CLIView cliView = new CLIView(cLIInfo);
        cliView.availableCommands();
    }

    public static void main1(String[] args) {
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
                        new CLIView(new CLIInfo(
                            game.getPlayers(), game.getPlayerFromColor(PlayerColor.PURPLE), PlayerColor.PURPLE, PlayerColor.PURPLE)
                        )
                ),
                new Pair<>(
                        PlayerColor.BLUE,
                        new CLIView(new CLIInfo(
                                game.getPlayers(), game.getPlayerFromColor(PlayerColor.BLUE), PlayerColor.BLUE, PlayerColor.BLUE)
                        )
                ),
                new Pair<>(
                        PlayerColor.YELLOW,
                        new CLIView(new CLIInfo(
                                game.getPlayers(), game.getPlayerFromColor(PlayerColor.YELLOW), PlayerColor.YELLOW, PlayerColor.YELLOW)
                        )
                )
        )
                .collect(Collectors.toMap(
                        p -> p.getFirst(),
                        p -> p.getSecond()
                ));

        Controller controller = new Controller(game, viewMap);
        game.register(viewMap.get(PlayerColor.PURPLE));
        viewMap.get(PlayerColor.PURPLE).register(controller);
        viewMap.get(PlayerColor.PURPLE).notify(new ShootRequest(PlayerColor.PURPLE, "heatseeker", PlayerColor.PURPLE));
    }
}