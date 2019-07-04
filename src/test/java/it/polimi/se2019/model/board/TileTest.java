package it.polimi.se2019.model.board;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class TileTest {
    @Test
    public void testGetDoors() {
        NormalTile tile1 = new NormalTile(TileColor.RED, Collections.singleton(
                Direction.NORTH
        ));
        NormalTile tile2 = new NormalTile(TileColor.RED, Collections.emptySet());
        NormalTile tile3 = new NormalTile(TileColor.RED, Arrays.stream(Direction.values()).collect(Collectors.toSet()));
        NormalTile tile4 = new NormalTile(TileColor.RED, new HashSet<>(Arrays.asList(
                Direction.EAST, Direction.WEST
        )));

        Boolean[] b1 = {true, false, false, false};
        Boolean[] b2 = {false, false, false, false};
        Boolean[] b3 = {true, true, true, true};
        Boolean[] b4 = {false, true, false, true};

        // TODO: update JUnit version to test this
        // assertArrayEquals(b1, tile1.getDoors());
    }

    @Test
    public void testGetDoorsDirections() {
        Tile testTile = new NormalTile(TileColor.BLUE, new HashSet<>(Arrays.asList(
                Direction.NORTH, Direction.WEST
        )));

        assertEquals(testTile.getDoorsDirections(),
                     new HashSet<>(Arrays.asList(Direction.NORTH, Direction.WEST)));
    }
}