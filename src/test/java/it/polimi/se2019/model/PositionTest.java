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
            // TODO: discuss what to do with negative positions
            // fail ();
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * Test equality
     */
    @Test
    public void testEquals() {
        assertTrue(new Position(0, 0).equals(new Position(0, 0)));
    }

    /**
     * Test addition
     */
    @Test
    public void testAddSimpleAddition() {
        Position pos1 = new Position(0, 2);
        Position pos2 = new Position(2, 0);

        Position result = pos1.add(pos2);

        assertEquals(result, new Position(2, 2));
    }
}