package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.JsonString;
import it.polimi.se2019.util.Jsons;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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
    private Board mGameBoard1;

    @Before
    public void instantiateExampleBoards() {
        // unit board (using setters)
        mExampleUnitBoard = new Board(1, 1);
        mExampleUnitBoard.setTileAt(new Position(0, 0), new NormalTile(TileColor.BLUE, 0));

        // empty board
        mExampleEmptyBoard = new Board(3, 3);

        // simple wall
        mExampleSimpleWallBoard = Board.fromJson(Jsons.get("boards/tests/simple_wall"));

        // game board 1
        mGameBoard1 = Board.fromJson(Jsons.get("boards/game/board1"));
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
    public void testCanSeeOneTileSameRoom() {
        assertTrue(mExampleUnitBoard.canSee(new Position(0), new Position(0)));
    }

    @Test
    public void testCanSeeInsideSameRoom() {
        // yellow room
        assertTrue(mGameBoard1.canSee(new Position(3, 2), new Position(2, 1)));
        assertTrue(mGameBoard1.canSee(new Position(2, 2), new Position(2, 1)));
        assertTrue(mGameBoard1.canSee(new Position(3, 1), new Position(2, 1)));
        assertTrue(mGameBoard1.canSee(new Position(2, 1), new Position(2, 1)));

        // blue room
        assertTrue(mGameBoard1.canSee(new Position(0, 0), new Position(0, 0)));
        assertTrue(mGameBoard1.canSee(new Position(1, 0), new Position(0, 0)));
        assertTrue(mGameBoard1.canSee(new Position(2, 0), new Position(0, 0)));

        // cannot see green 1x1 room from white 1x1 room in the opposite corner
        assertFalse(mGameBoard1.canSee(new Position(1, 2), new Position(3, 0)));
    }

    @Test
    public void testCanSeeRoomSeparatedByDoor() {
        // test that player standing near door in yellow 2x2 room can see player standing in blue 3x1 room above
        assertTrue(mGameBoard1.canSee(new Position(2, 0), new Position(2, 1)));

        // test that player standing near door in purple 2x1 room can see player standing in blue 3x1 room above
        assertTrue(mGameBoard1.canSee(new Position(0, 1), new Position(0, 0)));
    }

    @Test
    public void testGetRangeInfoTrivialRange() {
        RangeInfo rangeInfo = mExampleEmptyBoard.getRangeInfo(new Position(1, 1));

        RangeInfo expected = RangeInfo.fromMatrix(new Position(1),
                new Integer[][]{
                        { 2, 1,  2},
                        { 1, 0,  1},
                        { 2, 1,  2}
                },
                new Integer[][]{
                        { 1, 1, 1},
                        { 1, 1, 1},
                        { 1, 1, 1}
                }

        );

        assertEquals(expected, rangeInfo);
    }

    @Test
    public void testGetRangeInfoSimpleWall() {
        RangeInfo rangeInfo = mExampleSimpleWallBoard.getRangeInfo(new Position(1, 2));

        RangeInfo expected = RangeInfo.fromMatrix(new Position(1, 2),
                new Integer[][]{
                        {3,  4,  5, 4},
                        {2, -1, -1, 3},
                        {1,  0,  1, 2},
                        {2,  1,  2, 3}
                },
                new Integer[][]{
                        {1, 1, 1, 1},
                        {1, 0, 0, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1}
                }
        );

        assertEquals(expected, rangeInfo);
    }

    @Test
    public void testPosStream() {
        // generate
        Stream<Position> actual = mExampleSimpleWallBoard.posStream();

        // check
        Stream<Position> expected = Stream.of(
                new Position(0, 0),
                new Position(0, 1),
                new Position(0, 2),
                new Position(0, 3),
                new Position(1, 0),
                new Position(1, 1),
                new Position(1, 2),
                new Position(1, 3),
                new Position(2, 0),
                new Position(2, 1),
                new Position(2, 2),
                new Position(2, 3),
                new Position(3, 0),
                new Position(3, 1),
                new Position(3, 2),
                new Position(3, 3)
        );
        assertEquals(expected, actual);
    }
}
