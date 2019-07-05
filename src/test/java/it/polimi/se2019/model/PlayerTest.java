package it.polimi.se2019.model;

import it.polimi.se2019.controller.weapon.Weapon;
import it.polimi.se2019.controller.weapon.Weapons;
import org.junit.Test;

import static java.util.Arrays.fill;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testPlayer(){
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        AmmoValue testAmmo = new AmmoValue(1,1,1);

        assertEquals("testPlayer",player1.getName());
        assertEquals(testAmmo,player1.getAmmo());
    }

    @Test
    public void testIsFullOfWeapon(){
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);

        Weapon weapon1 = Weapons.get("heatseeker");
        Weapon weapon2 = Weapons.get("heatseeker");
        Weapon weapon3 = Weapons.get("heatseeker");
        try{
            player1.addWeapon(weapon1);
            player1.addWeapon(weapon2);
        }
        catch (FullHandException e){
            fail();
        }
        assertFalse(player1.isFullOfWeapons());
        try {
            player1.addWeapon(weapon3);
        }catch (FullHandException e){
            fail();
        }
        assertTrue(player1.isFullOfWeapons());
    }

    @Test
    public void testTakeWeapon(){
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);
        Weapon weapon1 = Weapons.get("heatseeker");
        Weapon weapon2 = Weapons.get("heatseeker");
        Weapon weapon3 = Weapons.get("heatseeker");
        try{
            player1.addWeapon(weapon1);
            player1.addWeapon(weapon2);
            player1.addWeapon(weapon3);
        }
        catch (FullHandException e){
            fail();
        }
        assertEquals(weapon2,player1.takeWeapon(1));
        assertNull(player1.getWeapon(1));
    }

    @Test
    public void getMaxGrabDistance(){
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;

        player1.onDamageTaken(new Damage(1,0), testAttackingPlayer);
        assertEquals(1,player1.getMaxGrabDistance());
        player1.onDamageTaken(new Damage(2,0), testAttackingPlayer);
        assertEquals(2,player1.getMaxGrabDistance());

    }


    @Test
    public void testCanMoveBeforeShooting(){
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;

        player1.onDamageTaken(new Damage(4,0), testAttackingPlayer);
        assertFalse(player1.canMoveBeforeShooting());
        player1.onDamageTaken(new Damage(2,0), testAttackingPlayer);
        assertTrue(player1.canMoveBeforeShooting());
    }


    @Test
    public void testAddScore() {
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);
        int currentScore = player1.getScore();

        player1.addScore(15);
        assertEquals(currentScore + 15,player1.getScore());
    }

    @Test
    public void testAddWeapon() {
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        Weapon weapon1 = Weapons.get("heatseeker");
        Weapon weapon2 = Weapons.get("heatseeker");
        Weapon weapon3 = Weapons.get("heatseeker");
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
        Weapon weapon1 = Weapons.get("heatseeker");
        Weapon weapon2 = Weapons.get("heatseeker");
        Weapon weapon3 = Weapons.get("heatseeker");
        Weapon weapon4 = Weapons.get("heatseeker");
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
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;

        PlayerColor[] testDamage = new PlayerColor[12];
        for (int i = 0; i < 8; i++) {
            testDamage[i] = testAttackingPlayer;
        }
        PlayerColor[] testDamage2 = new PlayerColor[12];

        player1.onDamageTaken(new Damage(0,3), testAttackingPlayer);
        player1.onDamageTaken(new Damage(0,0), testAttackingPlayer);
        // Marks should not activate if no damage is dealt
        assertArrayEquals(testDamage2, player1.getDamageTaken());

        player1.onDamageTaken(new Damage(5,0), testAttackingPlayer);
        assertArrayEquals(testDamage, player1.getDamageTaken());
        assertEquals(0, player1.getMarks().get(testAttackingPlayer).intValue());
    }

    @Test
    public void testSufferedMarks() {
        Player player1 = new Player("testPlayer",PlayerColor.BLUE);
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;
        int testMark1 = 2;
        int testMark2 = 3;

        player1.onDamageTaken(new Damage(0, testMark1), testAttackingPlayer);
        assertEquals(testMark1, player1.getMarks().get(testAttackingPlayer).intValue());
        player1.onDamageTaken(new Damage(0, testMark2), testAttackingPlayer);
        // Max marks possible for each player is 3
        assertEquals(3, player1.getMarks().get(testAttackingPlayer).intValue());
    }

    @Test
    public void testIsDead() {
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;

        player1.onDamageTaken(new Damage(9,0), testAttackingPlayer);
        assertFalse(player1.isDead());
        player1.onDamageTaken(new Damage(3,0), testAttackingPlayer);
        assertTrue(player1.isDead());
    }

    @Test
    public void testAddPowerUp() {

        Player player = new Player("Andrea", PlayerColor.GREY);
        PowerUpCard card1 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0));
        PowerUpCard card2 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,0,1));
        PowerUpCard card3 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0));

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
        PowerUpCard card1 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,0,1));
        PowerUpCard card2 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0));
        PowerUpCard card3 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0,1,0));

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
        PowerUpCard card1 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0, 1, 0));
        PowerUpCard card2 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0, 0, 1));
        PowerUpCard card3 = new PowerUpCard(PowerUpType.TELEPORT, new AmmoValue(0, 1, 0));

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
    public void testMove(){
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        Position testPos = new Position(2,1);

        player1.move(testPos);
        assertEquals(testPos,player1.getPos());
    }


    @Test
    public void testRespawnPlayer(){
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        PlayerColor[] testDamage = player1.getDamageTaken();
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;
        Position testPosition = new Position(1,0);
        PlayerColor[] nullVector = new PlayerColor[12];

        fill(testDamage,testAttackingPlayer);
        player1.onDamageTaken(new Damage(12,0), testAttackingPlayer);
        player1.respawn(testPosition);
        assertArrayEquals(nullVector,player1.getDamageTaken());
        assertEquals(testPosition,player1.getPos());
        assertFalse(player1.isDead());
    }

    @Test
    public void testOnDamageTaken(){
        Player player1 = new Player("testPlayer", PlayerColor.BLUE);
        Damage testDamage = new Damage(12,2);
        PlayerColor[] testDamage1 = player1.getDamageTaken();
        PlayerColor testAttackingPlayer = PlayerColor.YELLOW;

        fill(testDamage1,testAttackingPlayer);
        player1.onDamageTaken(testDamage,PlayerColor.YELLOW);
        assertArrayEquals(testDamage1,player1.getDamageTaken());
        assertEquals(testDamage.getMarksNum(), player1.getMarks().get(testAttackingPlayer).intValue());
        assertTrue(player1.isDead());
    }
}