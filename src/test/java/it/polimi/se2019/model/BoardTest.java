package it.polimi.se2019.model;

import com.google.gson.Gson;
import org.boon.Boon;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

import static junit.framework.TestCase.assertEquals;

public class BoardTest {

    private String mTestJsonString;
    private Map mBoardJsonMap;

    @Before
    public void instantiate() throws FileNotFoundException {
        Gson gson = new Gson();

        mTestJsonString = new Scanner(new File("resources/board1.json")).useDelimiter("\\Z").next();

        mBoardJsonMap = gson.fromJson(mTestJsonString, Map.class);
    }

    @Test
    public void testFromJsonSizeIsSame() {
        Board board = Board.fromJson(mTestJsonString);

        assertEquals(mBoardJsonMap.get("width"), board.getWidth());
        assertEquals(mBoardJsonMap.get("height"), board.getHeight());
    }

    @Test
    public void testFromJsonNonEmptyTilesNumIsSame() {
        // call tested function
        Board board = Board.fromJson(mTestJsonString);

        // in both cases the number of blank tiles is calculated by subtracting the number of
        // tiles that are there from the actual size of the board.
        int numOfBlankTilesInBoard = board.getSize() - board.getTiles().size();

        int jsonBoardSize = (int) mBoardJsonMap.get("width") * (int) mBoardJsonMap.get("height");
        int jsonBoardTilesNum = ((List) mBoardJsonMap.get("tiles")).size();
        int numOfBlankTilesInJson = jsonBoardSize - jsonBoardTilesNum;

        assertEquals(jsonBoardTilesNum, numOfBlankTilesInBoard);
    }

    @Test
    public void testFromJsonTilesAreTheSame() {
        // call tested function
        Board board = Board.fromJson(mTestJsonString);

        List jsonTiles = (List) mBoardJsonMap.get("tiles");

        for (int i = 0; i < board.getTiles().size(); i++) {
            Tile boardTile = board.getTiles().get(i);
            Map jsonTile = (Map) jsonTiles.get(i);

            // check type
            assertEquals((String) jsonTile.get("type"), boardTile.getTileType());

            // check position
            List jsonTilePos = (List) jsonTile.get("position");
            assertEquals(Board.itox(i), (int) jsonTilePos.get(0));
            assertEquals(Board.itoy(i), (int) jsonTilePos.get(1));



            // check color
            {
                "type"      : "normal",
                "position"      : [0, 0],
                "color"     : "blue",
                "doors"     : ["south"],
                "corridors" : ["east"]
            }
        }
}
