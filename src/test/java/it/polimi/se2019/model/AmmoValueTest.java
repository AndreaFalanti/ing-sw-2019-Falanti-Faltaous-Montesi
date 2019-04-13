package it.polimi.se2019.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AmmoValueTest {

    /**
     * Test constructor with valid parameters.
     */
    @Test
    public void testAmmoValueConstructor() {
        try {
            AmmoValue ammo1 = new AmmoValue(2, 3, 0);
            assertTrue(true);
        }
        catch (IllegalArgumentException e) {
            fail();
        }
    }

    /**
     * Test constructor with invalid parameters, it should throw an exception.
     */
    @Test
    public void testAmmoValueConstructorIllegalArgumentException() {
        try {
            AmmoValue ammo1 = new AmmoValue(4, 3, 1);
            fail ();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
        try {
            AmmoValue ammo1 = new AmmoValue(2, -3, 2);
            fail ();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test add operation and check that values above MAX_AMMO are clamped
     */
    @Test
    public void testAdd() {
        AmmoValue ammo1 = new AmmoValue(2, 1, 3);
        AmmoValue ammo2 = new AmmoValue(1, 0, 1);

        ammo1.add(ammo2);
        assertArrayEquals(new int[] {3, 1, 3},
                new int[] {ammo1.getRed(), ammo1.getYellow(), ammo1.getBlue()});
    }

    /**
     * Test subtract operation on a valid case, where all ammo1 values are greater or equals to ammo2 ones.
     */
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

    /**
     * Test subtract operation on an invalid case, at least one ammo1 value is lower that ammo2 one,
     * so check that exception is thrown.
     */
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