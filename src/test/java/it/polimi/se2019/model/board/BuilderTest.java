package it.polimi.se2019.model.board;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static junit.framework.TestCase.assertEquals;

public class BuilderTest {
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

    @Test
    void testCombineRightBothEmpty() {
        Builder builder1 = new Builder();
        Builder builder2 = new Builder();

        Builder combined = builder1.combineRight(builder2);

        assertEquals(new Board(), combined);
    }

    @Test
    void testCombineRightCombineWithEmpty() {

        Builder builder = new Builder().fromJson(mExampleBoardJsonString);
        Builder emptyBuilder = new Builder();

        Builder combined = builder.combineRight(emptyBuilder);

        // combined should be exactly equals to the first combined half
        // (since the second was empty)
        assertEquals(builder, combined);
    }

    @Test
    void testCombineRightCombineWithItself() {
        final String expectedBoardJsonString = "" +
                "{" +
                "   \"width\" : 2," +
                "   \"height\" : 1," +
                "   \"tiles\" : [" +
                "       {" +
                "           \"type\" : \"normal\"," +
                "           \"position\" : [0, 0]" +
                "           \"color\" : \"blue\"," +
                "           \"doors\" : []" +
                "       }," +
                "       {" +
                "           \"type\" : \"normal\"," +
                "           \"position\" : [1, 0]" +
                "           \"color\" : \"blue\"," +
                "           \"doors\" : []" +
                "       }" +
                "   ]" +
                "}";

        Builder builder = new Builder().fromJson(mExampleBoardJsonString);
        Builder builderTwin = builder.clone();

        Builder combined = builder.combineRight(builderTwin);

        assertEquals(new Builder().fromJson(expectedBoardJsonString), combined);
    }
}
