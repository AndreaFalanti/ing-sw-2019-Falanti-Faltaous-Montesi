package it.polimi.se2019.model.board;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BuilderTest {
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

    @Test
    public void testCombineRightBothEmpty() {
        Builder builder1 = new Builder();
        Builder builder2 = new Builder();

        Builder combined = builder1.combineRight(builder2.build());

        assertEquals(new Builder(), combined);
    }

    @Test
    public void testCombineRightCombineWithItself() {
        final String expectedBoardJsonString = "" +
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
                "           \"type\" : \"normal\"," +
                "           \"color\" : \"BLUE\"," +
                "           \"doors\" : []" +
                "       }" +
                "   ]" +
                "}";

        Builder builder = new Builder().fromJson(mExampleBoardJsonString);
        Builder builderTwin = builder.deepCopy();

        Builder combined = builder.combineRight(builderTwin.build());

        assertEquals(new Builder().fromJson(expectedBoardJsonString), combined);
    }
}
