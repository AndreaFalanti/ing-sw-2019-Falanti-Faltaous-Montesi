package it.polimi.se2019.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.fill;
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
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        MachineGun weapon1 = new MachineGun("Weapon1");
        MachineGun weapon2 = new MachineGun("Weapon2");
        MachineGun weapon3 = new MachineGun("Weapon3");
        try{
            player1.addWeapon(weapon1);
            player1.addWeapon(weapon2);
            player1.addWeapon(weapon3);
            assertArrayEquals(new Weapon[] {weapon1,weapon2,weapon3},player1.getWeapons());
        }
        catch (FullHandException e){
            fail();
        }
    }

    @Test
    public void testAddWeaponFullHandException() {
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        MachineGun weapon1 = new MachineGun("Weapon1");
        MachineGun weapon2 = new MachineGun("Weapon2");
        MachineGun weapon3 = new MachineGun("Weapon3");
        MachineGun weapon4 = new MachineGun("Weapon4");
        try{
            player1.addWeapon(weapon1);
            player1.addWeapon(weapon2);
            player1.addWeapon(weapon3);
            player1.addWeapon(weapon4);
            fail();
        }
        catch (FullHandException e){
            assertTrue(true);
        }
    }

    @Test
    public void testSufferDamage() {
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        PlayerColor[] testDamage = player1.getDamageTaken();

        fill(testDamage,PlayerColor.YELLOW);
        player1.sufferedDamage(PlayerColor.YELLOW, 12);
        assertArrayEquals(player1.getDamageTaken(),testDamage);
    }

    @Test
    public void testIsDead() {
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        player1.isDead();
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