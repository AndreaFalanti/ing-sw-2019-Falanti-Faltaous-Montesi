package it.polimi.se2019.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {
    /**
     * Test for constructor with valid parameters
     */
    @Test
    public void testPositionConstructor () {
        try {
            Position pos = new Position(0, 4);
            assertTrue(true);
        }
        catch (IllegalArgumentException e) {
            fail ();
        }
    }

    /**
     * Test for constructor with invalid parameters, it should throw an exception
     */
    @Test
    public void testPositionConstructorIllegalArgumentException () {
        try {
            Position pos = new Position(-1, 0);
            fail ();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }
}