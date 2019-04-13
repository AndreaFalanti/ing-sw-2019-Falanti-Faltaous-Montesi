package it.polimi.se2019.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AmmoValueTest {

    @Test
    public void testAdd() {
        AmmoValue ammo1 = new AmmoValue(2, 1, 3);
        AmmoValue ammo2 = new AmmoValue(1, 0, 1);

        ammo1.add(ammo2);
        assertArrayEquals(new int[] {3, 1, 3},
                new int[] {ammo1.getRed(), ammo1.getYellow(), ammo1.getBlue()});
    }

    @Test
    public void testSubtract() {
        AmmoValue ammo1 = new AmmoValue(2, 1, 3);
        AmmoValue ammo2 = new AmmoValue(1, 0, 1);

        try {
            ammo1.subtract(ammo2);
        }
        catch (NotEnoughAmmoException e) {
            fail ();
        }

        assertArrayEquals( new int[] {1, 1, 2},
                new int[] {ammo1.getRed(), ammo1.getYellow(), ammo1.getBlue()});
    }

    @Test
    public void testSubtractNoEnoughAmmoException() {
        AmmoValue ammo1 = new AmmoValue(2, 1, 3);
        AmmoValue ammo2 = new AmmoValue(1, 3, 1);

        try {
            ammo1.subtract(ammo2);
            fail ();
        }
        catch (NotEnoughAmmoException e) {
            assertTrue(true);
        }
    }
}