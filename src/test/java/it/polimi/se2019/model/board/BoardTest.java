<<<<<<< HEAD
=======
package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.JsonString;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class BoardTest {
    // example json strings
    private final String mExampleBoardJsonString = "" +
        "{" +
        "   \"width\" : 1," +
        "   \"height\" : 1," +
        "   \"tiles\" : [" +
        "       {" +
        "           \"type\" : \"normal\"," +
        "           \"color\" : \"BLUE\"," +
        "           \"doors\" : []" +
        "       }" +
        "   ]" +
        "}";

    // example boards used in various tests
    private Board mExampleUnitBoard;
    private Board mExampleEmptyBoard;

    @Before
    public void instantiateExampleBoards() {
        // unit board (using setters)
        mExampleUnitBoard = new Board(1, 1);
        mExampleUnitBoard.setTileAt(new Position(0, 0),
                                new NormalTile(TileColor.BLUE, 0));

        // empty board
        mExampleEmptyBoard = new Board(3, 3);
    }

    @Before
    public void instantiateExampleComplexBoard() {
        mExampleEmptyBoard = Board.fromJson();
    }

    @Test
    public void testFromJsonBoardWithOneTile() {
        Board testBoard = Board.fromJson(mExampleBoardJsonString);

        assertEquals(mExampleUnitBoard, testBoard);
    }

    @Test
    public void testToJsonBoardWithOneTile() {
        assertEquals(new JsonString(mExampleBoardJsonString),
                     new JsonString(mExampleUnitBoard.toJson()));
    }

    @Test
    public void testEqualsSameDeserializedStringYieldsEqualBoard() {
        Board board1 = Board.fromJson(mExampleBoardJsonString);
        Board board2 = Board.fromJson(mExampleBoardJsonString);

        assertTrue(board1.equals(board2));
    }

    @Test
    public void testDeepCopyCloneIsEqualToOriginal() {
        Board board = Board.fromJson(mExampleBoardJsonString);

        Board clonedBoard = board.deepCopy();

        assertEquals(board, clonedBoard);
    }

    @Test
    public void getTileFromPosition() {
        Position pos = new Position(0, 0);
        Board board = Board.fromJson(mExampleBoardJsonString);

        Tile tile = board.getTileAt(pos);
        assertEquals(board.getTiles().get(0), tile);
    }

    @Test
    public void testGetRows() {
    }

    @Test
    public void getTileDistance() {
        //TODO: we need a valid board to test this method (at least 3 * 3)
    }

    @Test
    // TODO: experiment with parameterized tests for this
    public void testIsOutOfBounds() {
        Board board = new Board(3, 3);

        assertTrue(board.isOutOfBounds(new Position(4, 0)));
    }

    @Test
    public void testGetCellsInRangeTrivialRange() {
        RangeInfo rangeInfo = mExampleEmptyBoard.getCellsWithinRange(new Position(1, 1), 1);

        assertEquals(
                rangeInfo,
                RangeInfo.fromString(
                        "1 1 1" +
                        "1 0 1" +
                        "1 1 1"
                )
        );
    }

    @Test
    public void testGetCellsInRangeSimpleWall() {
        RangeInfo rangeInfo = mExampleSimpleWallBoard.getCellsWithinRange(new Position(1, 1), 1);

        assertEquals(
                rangeInfo,
                RangeInfo.fromString(
                        "3 4 5 4" +
                        "2 # # 3" +
                        "1 0 1 2"
                )
        );
}
>>>>>>> Add some needed Board utilities; start implementation of getRangeInfo
