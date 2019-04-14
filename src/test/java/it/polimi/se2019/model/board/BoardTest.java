package it.polimi.se2019.model.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.model.Position;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        "           \"position\" : [0, 0]" +
        "           \"color\" : \"blue\"," +
        "           \"doors\" : []" +
        "       }" +
        "   ]" +
        "}";

    private String mJsonString;
    private Gson mBoardGson;

    @Before
    public void instantiateBoardGson() {
        mBoardGson = new GsonBuilder()
                .registerTypeAdapter(Board.class, new DoorsDeserializer())
                .create();
    }

    @Before
    public void instantiateJsonString() throws FileNotFoundException {
        mJsonString = new Scanner(new File("resources/board1.json")).useDelimiter("\\Z").next();
    }

    @Test
    public void testFromJson() {
        assertEquals(mBoardGson.fromJson(mJsonString, Board.class),
                     Board.fromJson(mJsonString));
    }

    @Test
    public void testEqualsSameDeserializedStringYieldsEqualBoard() {
        Board board1 = Board.fromJson(mExampleBoardJsonString);
        Board board2 = Board.fromJson(mExampleBoardJsonString);

        assertTrue(board1.equals(board2));
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

        Tile tile = board.getTileFromPosition(pos);
        assertEquals(board.getTiles().get(0), tile);
    }

    @Test
    public void getTileDistance() {
        //TODO: we need a valid board to test this method (at least 3 * 3)
    }
}
