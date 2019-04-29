package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class RangeInfoTest {
    private RangeInfo mTestRangeInfoOneByOne;
    private RangeInfo mTestRangeInfoTwoByTwo;

    // @Before
    // public void instantiateTestRangeInfos() {
        // mTestRangeInfoOneByOne = RangeInfo.fromMatrix(new Position(0, 0),
                // new Integer[][]{
                        // {0}
                // },
                // new  Boolean[][]{
                        // {true}
                // });
        // mTestRangeInfoTwoByTwo = RangeInfo.fromMatrix(new Position(0, 1),
        // new int[][]{
                // { 0, 1 },
                // { 1, 2 }
        // });
    // }

    // @Test
    // public void testEquals() {
        // assertEquals(mTestRangeInfoTwoByTwo, RangeInfo.fromMatrix(new Position(0, 1),
        // new int[][]{
                // { 0, 1 },
                // { 1, 2 }
        // }));
    // }

    @Test
    public void testFromMatrixOneByOne() {
        RangeInfo expected = new RangeInfo();
        expected.addDistAt(new Position(0, 0), 0);

        assertEquals(expected, mTestRangeInfoOneByOne);
    }

    @Test
    public void testFromMatrixTwoByTwo() {
        RangeInfo expected = new RangeInfo(new Position(0, 1));
        expected.addDistAt(new Position(0, 1), 0);
        expected.addDistAt(new Position(0, 2), 1);
        expected.addDistAt(new Position(1, 1), 1);
        expected.addDistAt(new Position(1, 2), 2);

        assertEquals(expected, mTestRangeInfoTwoByTwo);
    }

    @Test
    public void testToStringOneByOne() {
        String expected = "" +
                "[0, 0]\n" +
                "0";

        assertEquals(expected, mTestRangeInfoOneByOne.toString());
    }

    @Test
    public void testToStringTwoByTwo() {
        String expected = "" +
                "[0, 1]\n" +
                "# # # # #\n" +
                "# # # # #\n" +
                "# # 0 1 #\n" +
                "# # 1 2 #\n" +
                "# # # # #";

        assertEquals(expected, mTestRangeInfoTwoByTwo.toString());
    }
    
    @Test
    public void testIsVisited() {
        RangeInfo testRangeInfo = new RangeInfo();

        testRangeInfo.addDistAt(new Position(0, 0), 0);

        assertTrue(testRangeInfo.isVisited(new Position(0, 0)));
    }
}
