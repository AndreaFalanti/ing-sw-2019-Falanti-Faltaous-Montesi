package it.polimi.se2019.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void addWeapon() {
        Player player = new Player("Andrea", PlayerColor.GREY);
        //TODO: weapon class need to be refined with a new structure, for now is just a placeholder
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