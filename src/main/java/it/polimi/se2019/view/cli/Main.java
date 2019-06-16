package it.polimi.se2019.view.cli;

import it.polimi.se2019.model.*;

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
     /*   Weapon weapon1 = Weapons.get("heatseeker");
        Weapon weapon2 = Weapons.get("heatseeker");
        Weapon weapon3 = Weapons.get("heatseeker");
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
        owner.addWeapon(weapon2);*/
        owner.onDamageTaken(new Damage(3,0), PlayerColor.YELLOW);
        owner.onDamageTaken(new Damage(3,1), PlayerColor.BLUE);
        owner.onDamageTaken(new Damage(3,0), PlayerColor.YELLOW);
        owner.onDamageTaken(new Damage(3,2), PlayerColor.GREY);
        player2.onDamageTaken(new Damage(3,0), PlayerColor.YELLOW);
        player2.onDamageTaken(new Damage(3,1), PlayerColor.YELLOW);
        player3.onDamageTaken(new Damage(3,2), PlayerColor.YELLOW);
        PowerUpCard card1 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card2 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card3 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
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
      //  Board board= new Board();
        //end test

        CLIInfo cLIInfo = new CLIInfo(mPlayers,owner,ownerColor,activePlayer);
        CLIView cliView = new CLIView(cLIInfo);
        cliView.availableCommands();
    }
}