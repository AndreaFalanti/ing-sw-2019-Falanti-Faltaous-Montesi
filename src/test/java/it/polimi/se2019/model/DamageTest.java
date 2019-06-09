package it.polimi.se2019.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DamageTest {
    /**
     * Test constructor with valid parameters.
     */
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

    /**
     * Test parsing a string with all info into a Damage value
     */
    @Test
    public void testFromStringAllColors() {
        Damage expected = new Damage(1, 2);

        Damage actual = Damage.from("1d2m")
                .orElseGet(() -> {
                    fail();
                    return null;
                });

        assertEquals(expected, actual);
    }

    /**
     * Test parsing a string with some missing info into a Damage value
     */
    @Test
    public void testFromStringMissingColors() {
        Damage expected = new Damage(0, 1);

        Damage actual = Damage.from("1m")
                .orElseGet(() -> {
                    fail();
                    return null;
                });

        assertEquals(expected, actual);
    }

    /**
     * Assert that malformed strings are not parsed as correct ones
     */
    @Test
    public void testFromStringWrongOrderShouldFail() {
        assertFalse(Damage.from("3m3d").isPresent());
    }

    /**
     * Test constructor with invalid parameters, it should throw an exception.
     */
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