package it.polimi.se2019.view.cli;

import it.polimi.se2019.network.client.NetworkHandler;

import java.io.IOException;
import java.rmi.NotBoundException;

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
        LoginCLI.log(view);
        new Thread(() -> ((NetworkHandler)view.getNetworkHandler()).startReceivingMessages()).start();
    }
}