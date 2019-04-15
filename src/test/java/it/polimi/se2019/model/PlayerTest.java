package it.polimi.se2019.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testAddScore() {
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);
        int currentScore = player1.getScore();

        player1.addScore(15);
        assertEquals(currentScore + 15,player1.getScore());
    }

    @Test
    public void testIncrementDeaths() {
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);

        player1.incrementDeaths();
        assertEquals(1,player1.getDeathsNum());

    }
    @Test
    public void testAddWeapon() {
        Player player = new Player("testPlayer", PlayerColor.BLUE);

        try{
            player.addWeapon();
        }
        catch (fullHandException e){
            fail();
        }
    }

    @Test
    public void addPowerUp() {
        //TODO: complete this test, need to check for an exception if hand is full?
        /*Player player = new Player("Andrea", PlayerColor.GREY);
        PowerUpCard card1 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card2 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card3 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card4 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);

        player.addPowerUp(card1);
        player.addPowerUp(card2);
        player.addPowerUp(card3);*/

    }

    @Test
    public void move() {
    }
}