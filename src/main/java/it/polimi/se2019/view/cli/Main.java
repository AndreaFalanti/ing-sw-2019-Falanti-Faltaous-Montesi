package it.polimi.se2019.view.cli;

import it.polimi.se2019.controller.Controller;
import it.polimi.se2019.model.*;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.network.client.ClientInterface;
import it.polimi.se2019.network.client.ClientNetworkHandler;
import it.polimi.se2019.network.client.NetworkHandler;
import it.polimi.se2019.network.client.RmiClient;
import it.polimi.se2019.network.server.SocketConnection;
import it.polimi.se2019.util.Jsons;
import it.polimi.se2019.util.Pair;
import it.polimi.se2019.view.View;
import it.polimi.se2019.view.request.ShootRequest;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {


    public static void main(String[] args) throws IOException, NotBoundException {
        // test
     /*   List<Player> mPlayers = new ArrayList<>() ;
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

        Game game = new Game(
                Board.fromJson(Jsons.get("boards/game/board1")),
                new ArrayList<>(Arrays.asList(
                        owner,player1,player2,player3,player4
                )),
                1
        );*/
        //end test

        CLIView view = new CLIView(null);
        Scanner scanner = new Scanner(System.in);


        System.out.println("Choose client connection type: ");
        System.out.println(("Press 1 for socket"));
        System.out.println(("Press 2 for rmi"));
        System.out.println((">> "));

        int result = -1;
        boolean validCmd;
        do {
            try {
                result = scanner.nextInt();
                if (result < 1 || result > 2) {
                    System.out.println(("Invalid input"));
                    System.out.println(("\n>> "));
                    validCmd = false;
                } else {
                    validCmd = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(("Choose a number please"));
                System.out.println(("\n>> "));
                // flush remaining \n in buffer
                scanner.next();
                validCmd = false;
            }
        } while (!validCmd);

        ClientNetworkHandler mNetworkHandler;
        ClientInterface client;
        boolean isValid=false;
        switch (result) {

            case 1:

                mNetworkHandler = new NetworkHandler(
                        view,
                        SocketConnection.establish("localhost", 4567)
                );
                String username;
                Scanner scanner1 = new Scanner(System.in);
                while(!isValid){
                    System.out.println("Choose username");
                    username=scanner1.nextLine();
                    if (mNetworkHandler.sendUsername(username)) {
                        view.setNetworkHandler(mNetworkHandler);
                        ((NetworkHandler)mNetworkHandler).startReceivingMessages();
                        isValid=true;
                    }
                    else {
                        System.out.print("username already used");
                    }
                }

                break;
            case 2:
                client = new RmiClient("localhost", 4568);
                break;
            default:
                throw new IllegalStateException("invalid client selected");
        }




    }




    public static void main3(String[] args) {
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
                            game.extractViewInitializationInfo(PlayerColor.PURPLE),PlayerColor.PURPLE
                        )
                ),
                new Pair<>(
                        PlayerColor.BLUE,
                        new CLIView(
                                game.extractViewInitializationInfo(PlayerColor.PURPLE),PlayerColor.BLUE
                        )
                ),
                new Pair<>(
                        PlayerColor.YELLOW,
                        new CLIView(
                                game.extractViewInitializationInfo(PlayerColor.YELLOW),PlayerColor.YELLOW
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
      //  viewMap.get(PlayerColor.YELLOW).reinitialize(game.extractViewInitializationInfo().setOwnerColor(PlayerColor.YELLOW));
        CLIView cli = (CLIView)viewMap.get(PlayerColor.YELLOW);
        cli.availableCommands();
      //  System.out.println(controller.getGame().getPlayerFromColor(PlayerColor.BLUE).getDamageTaken());
    }
}