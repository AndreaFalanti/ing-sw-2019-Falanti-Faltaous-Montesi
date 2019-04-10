package it.polimi.se2019.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DamageTest {
    @Test
    public void testDamageConstructor () {
        try {
            Damage damage = new Damage(3, 0);
            assertTrue(true);
        }
        catch (IllegalArgumentException e) {
            fail();
        }
    }

    @Test
    public void testDamageConstructorIllegalArgumentException () {
        try {
            Damage damage = new Damage(-2, 1);
            fail();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}