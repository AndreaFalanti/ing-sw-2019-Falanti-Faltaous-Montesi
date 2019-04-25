package it.polimi.se2019.model;

import org.junit.Test;

//import javax.swing.text.Position;

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
    public void testSufferedDamage() {
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        PlayerColor[] testDamage = player1.getDamageTaken();
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;

        fill(testDamage,testAttackingPlayer);
        player1.sufferedMarks(testAttackingPlayer,3);
        player1.sufferedDamage(testAttackingPlayer, 12 + player1.getMarks().get(testAttackingPlayer));
        assertArrayEquals(player1.getDamageTaken(),testDamage);
        assertTrue(player1.getMarks().get(testAttackingPlayer) == 0);
    }

    @Test
    public void testSufferedMarks() {
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;
        int testMark1 = 2;
        int testMark2 = 3;

        player1.sufferedMarks(testAttackingPlayer,testMark1);
        assertTrue(testMark1 == player1.getMarks().get(testAttackingPlayer));
        player1.sufferedMarks(testAttackingPlayer,testMark2);
        assertTrue(testMark2 == player1.getMarks().get(testAttackingPlayer));
    }

    @Test
    public void testIsDead() {
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);

        player1.sufferedDamage(PlayerColor.YELLOW, 9);
        player1.setDeadStatus();
        assertFalse(player1.isDead());
        player1.sufferedDamage(PlayerColor.YELLOW, 3);
        player1.setDeadStatus();
        assertTrue(player1.isDead());
    }

    @Test
    public void testAddPowerUp() {

        Player player = new Player("Andrea", PlayerColor.GREY);
        PowerUpCard card1 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card2 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card3 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);

        try {
            player.addPowerUp(card1);
            player.addPowerUp(card2);
            player.addPowerUp(card3);
            assertArrayEquals(new PowerUpCard[] {card1, card2, card3, null}, player.getPowerUps());
        }
        catch (FullHandException e){
            fail();
        }

        try {
            player.addPowerUp(card3, true);
            assertTrue(true);
        }
        catch (FullHandException e) {
            fail();
        }
    }

    @Test
    public void testAddPowerUpFullHandException() {
        Player player = new Player("Andrea", PlayerColor.GREY);
        PowerUpCard card1 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card2 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);
        PowerUpCard card3 = new PowerUpCard("Teleport", new AmmoValue(0,1,0), null);

        try {
            player.addPowerUp(card1);
            player.addPowerUp(card2);
            player.addPowerUp(card3);
            player.addPowerUp(card3);
            fail();
        }
        catch (FullHandException e){
            assertTrue(true);
        }
    }

    @Test
    public void testDiscard() {
        Player player = new Player("Andrea", PlayerColor.GREY);
        PowerUpCard card1 = new PowerUpCard("Teleport", new AmmoValue(0, 1, 0), null);
        PowerUpCard card2 = new PowerUpCard("Teleport", new AmmoValue(0, 1, 0), null);
        PowerUpCard card3 = new PowerUpCard("Teleport", new AmmoValue(0, 1, 0), null);

        try {
            player.addPowerUp(card1);
            player.addPowerUp(card2);
            player.addPowerUp(card3);
        }
        catch (FullHandException e) {
            fail();
        }
        player.discard(card2);
        assertNull(player.getPowerUps()[1]);
        assertEquals(card1, player.getPowerUps()[0]);
    }

    @Test
    public void testRespawnPlayer(){
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        PlayerColor[] testDamage = player1.getDamageTaken();
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;
        Position testPosition = new Position(1,0);
        PlayerColor[] nullVector = new PlayerColor[12];

        fill(testDamage,testAttackingPlayer);
        player1.sufferedDamage(testAttackingPlayer, 12);
        player1.setDeadStatus();
        player1.respawnPlayer(testPosition);
        assertArrayEquals(nullVector,player1.getDamageTaken());
        assertEquals(testPosition,player1.getPos());
        assertFalse(player1.isDead());
    }

}