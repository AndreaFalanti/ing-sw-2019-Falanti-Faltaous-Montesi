package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class RangeInfoTest {
    private RangeInfo mTestRangeInfoOneByOne;
    private RangeInfo mTestRangeInfoTwoByTwo;

    @Before
    public void instantiateTestRangeInfos() {
        mTestRangeInfoOneByOne = RangeInfo.fromMatrix(new Position(0, 0),
                new Integer[][]{
                        {0}
                },
                new  Integer[][]{
                        {1}
                });
        mTestRangeInfoTwoByTwo = RangeInfo.fromMatrix(new Position(0, 1),
                new Integer[][]{
                        { 0, 1 },
                        { 1, 2 }
                },
                new Integer[][]{
                        { 1, 1 },
                        { 1, 1 }
                });
    }

    @Test
    public void testFromMatrix() {
        RangeInfo tested = RangeInfo.fromMatrix(new Position(0, 1),
                new Integer[][]{
                        { 0, 1 },
                        { 1, 2 }
                },
                new Integer[][]{
                        { 1, 1 },
                        { 1, 1 }
                });

        assertEquals(0, tested.getDistAt(new Position(0, 1)));
        assertEquals(1, tested.getDistAt(new Position(1, 1)));
        assertEquals(1, tested.getDistAt(new Position(0, 2)));
        assertEquals(2, tested.getDistAt(new Position(1, 2)));

        assertTrue(tested.isVisibleAt(new Position(0, 1)));
        assertTrue(tested.isVisibleAt(new Position(1, 1)));
        assertTrue(tested.isVisibleAt(new Position(0, 2)));
        assertTrue(tested.isVisibleAt(new Position(1, 2)));
    }

    @Test
    public void testAddDistAt() {
        mTestRangeInfoTwoByTwo.addDistAt(new Position(0, 0), 2);

        assertEquals(2, mTestRangeInfoTwoByTwo.getDistAt(new Position(0, 0)));
    }

    @Test
    public void testEquals() {
        assertEquals(mTestRangeInfoTwoByTwo, RangeInfo.fromMatrix(new Position(0, 1),
                new Integer[][]{
                        { 0, 1 },
                        { 1, 2 }
                },
                new Integer[][]{
                        { 1, 1 },
                        { 1, 1 }
                }));
    }

    @Test
    public void testToStringOneByOne() {
        String expected = "" +
                "[0, 0]\n" +
                "0^\n";

        assertEquals(expected, mTestRangeInfoOneByOne.toString());
    }

    @Test
    public void testToStringTwoByTwo() {
        String expected = "" +
                "[0, 1]\n" +
                "#  #  #  #  # \n" +
                "#  #  #  #  # \n" +
                "#  #  0^ 1^ # \n" +
                "#  #  1^ 2^ # \n" +
                "#  #  #  #  # \n";

        assertEquals(expected, mTestRangeInfoTwoByTwo.toString());
    }
    
    @Test
    public void testIsVisited() {
        RangeInfo testRangeInfo = new RangeInfo();

        testRangeInfo.addDistAt(new Position(0, 0), 0);

        assertTrue(testRangeInfo.isVisited(new Position(0, 0)));
    }
}
