package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.JsonString;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

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
    private Board mExampleSimpleWallBoard;

    @Before
    public void instantiateExampleBoards() {
        // unit board (using setters)
        mExampleUnitBoard = new Board(1, 1);
        mExampleUnitBoard.setTileAt(new Position(0, 0),
                                new NormalTile(TileColor.BLUE, 0));

        // empty board
        mExampleEmptyBoard = new Board(3, 3);

        // simple wall
        mExampleSimpleWallBoard = Board.fromJson(Jsons.get("boards/tests/simple_wall"));
    }

    @Test
    public void testFromJsonBoardWithOneTile() {
        Board testBoard = Board.fromJson(mExampleBoardJsonString);

        assertEquals(mExampleUnitBoard, testBoard);
    }

    @Test
    public void testFromJsonEmptyBoard() {
        String testJsonString = "" +
                "{" +
                "   \"width\" : 1," +
                "   \"height\" : 1," +
                "   \"tiles\" : [" +
                "       {" +
                "           \"type\" : \"empty\"" +
                "       }" +
                "   ]" +
                "}";

        Board expectedBoard = new Board(1, 1);
        expectedBoard.setTileAt(new Position(0, 0), null);

        assertEquals(expectedBoard,
                     Board.fromJson(testJsonString));
    }

    @Test
    public void testFromJsonBoardWithEmptyTile() {
        String testJsonString = "" +
                "{" +
                "   \"width\" : 2," +
                "   \"height\" : 1," +
                "   \"tiles\" : [" +
                "       {" +
                "           \"type\" : \"normal\"," +
                "           \"color\" : \"BLUE\"," +
                "           \"doors\" : []" +
                "       }," +
                "       {" +
                "           \"type\" : \"empty\"" +
                "       }" +
                "   ]" +
                "}";

        Board expectedBoard = new Board(2, 1);
        expectedBoard.setTileAt(new Position(0, 0), new NormalTile(TileColor.BLUE));
        expectedBoard.setTileAt(new Position(1, 0), null);

        assertEquals(expectedBoard, Board.fromJson(testJsonString));
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
    public void testGetRangeInfoTrivialRange() {
        RangeInfo rangeInfo = mExampleEmptyBoard.getRangeInfo(new Position(1, 1), 1);

        assertEquals(
                RangeInfo.fromMatrix(new Position(1),
                        new int[][]{
                                // N.B. -1 means impassable (i.e. wall/empty) or over the range specified
                                {-1, 1, -1},
                                { 1, 0,  1},
                                {-1, 1, -1}
                        }
                ),
                rangeInfo
        );
    }

    @Test
    public void testGetRangeInfoSimpleWall() {
        RangeInfo rangeInfo = mExampleSimpleWallBoard.getRangeInfo(new Position(1, 2), 5);

        RangeInfo expected = RangeInfo.fromMatrix(new Position(1, 2),
                new int[][]{
                        // N.B. -1 means impassable (i.e. wall/empty) or over the range specified
                        {3,  4,  5, 4},
                        {2, -1, -1, 3},
                        {1,  0,  1, 2},
                        {2,  1,  2, 3}
                }
        );

        assertEquals(expected, rangeInfo);
    }
}
