package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.JsonString;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class BoardTest {
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
    private Board mExampleBoard;

    @Before
    public void instantiateExampleBoard() {
        mExampleBoard = new Board(1, 1);
        mExampleBoard.setTileAt(new Position(0, 0),
                                new NormalTile(TileColor.BLUE, 0));
    }

    @Test
    public void testFromJsonBoardWithOneTile() {
        Board testBoard = Board.fromJson(mExampleBoardJsonString);
        assertEquals(mExampleBoard, testBoard);
    }

    @Test
    public void testToJsonBoardWithOneTile() {
        assertEquals(new JsonString(mExampleBoardJsonString),
                     new JsonString(mExampleBoard.toJson()));
    }

    @Test
    public void testEqualsSameDeserializedStringYieldsEqualBoard() {
        Board board1 = Board.fromJson(mExampleBoardJsonString);
        Board board2 = Board.fromJson(mExampleBoardJsonString);

        assertTrue(board1.equals(board2));
    }

    @Test
    public void testEqualsClonesAreEqual() {
        Board clone = mExampleBoard.deepCopy();

        assertEquals(mExampleBoard, clone);
    }

    @Test
    public void testDeepCopy() {
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
    public void getTileDistance() {
        //TODO: we need a valid board to test this method (at least 3 * 3)
    }
}
