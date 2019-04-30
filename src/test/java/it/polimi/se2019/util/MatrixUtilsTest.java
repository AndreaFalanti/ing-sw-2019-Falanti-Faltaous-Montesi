package it.polimi.se2019.util;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class MatrixUtilsTest {
    Integer[][] mZeroMatrix;
    Integer[][] mCountMatrix;

    @Before
    public void instantiate() {
        mZeroMatrix = new Integer[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        mCountMatrix = new Integer[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
    }

    @Test
    public void testToPrettyString() {
        String expected = "" +
                "1^ 2  3 \n" +
                "4  5^ 6 \n" +
                "7  8  9^\n";

        assertEquals(expected, MatrixUtils.toPrettyString(mCountMatrix, (ele, pos) -> {
            char diagonalEqualChar = (pos.getY() == pos.getX() ? '^' : ' ');
            return ele.toString() + diagonalEqualChar + ' ';
        }));
    }

    @Test
    public void testFillRectangularMatrix() {
        Function<Integer[][], String> prettify = (matrix) ->
                MatrixUtils.toPrettyString(matrix, (ele, pos) -> ele.toString());

        Integer[][] actual = new Integer[3][3];
        MatrixUtils.fillRectangularMatrix(actual, 3, 3, 0);

        assertEquals(prettify.apply(mZeroMatrix), prettify.apply(actual));
    }
}
