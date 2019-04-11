package it.polimi.se2019.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.model.board.Board;
import it.polimi.se2019.model.board.DoorsDeserializer;
import org.boon.Boon;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;

public class BoardTest {

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
}
