package it.polimi.se2019.model.board;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TileTest {
    @Test
    public void testGetDoors() {
        NormalTile tile1 = new NormalTile(TileColor.RED, 8);
        NormalTile tile2 = new NormalTile(TileColor.RED, 0);
        NormalTile tile3 = new NormalTile(TileColor.RED, 15);
        NormalTile tile4 = new NormalTile(TileColor.RED, 5);

        boolean[] b1 = {true, false, false, false};
        boolean[] b2 = {false, false, false, false};
        boolean[] b3 = {true, true, true, true};
        boolean[] b4 = {false, true, false, true};

        //TODO: JUnit 4.12 is required to test easily booleans array, should we update?
        //assertArrayEquals(b1, tile1.getDoors());
    }

    @Test
    public void testGetDoorsDirections() {
        Tile testTile = new NormalTile(TileColor.BLUE, 0b1000 + 0b0001);

        assertEquals(testTile.getDoorsDirections(),
                     Arrays.asList(Direction.NORTH, Direction.WEST));
    }
}