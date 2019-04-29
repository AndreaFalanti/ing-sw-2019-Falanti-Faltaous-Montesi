package it.polimi.se2019.util;

import it.polimi.se2019.model.Position;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MatrixUtils {
    /**
     * This class is meant as a static wrapper for utility methods
     */
    private MatrixUtils() {}

    /**
     * Checks if matrix is rectangular (a null matrix is not rectangular)
     * @param toCheck the matrix to check
     * @param <T> the type of the elements contained by the matrix
     * @return true if the matrix is rectangular
     */
    public static <T> boolean isValidRectangularMatrix(T[][] toCheck) {
        if (toCheck == null)
            return false;

        if (toCheck[0] == null)
            return false;

        int rowLength = toCheck[0].length;
        for (int i = 1; i < toCheck.length; ++i) {
            T[] row = toCheck[i];

            if (row == null)
                return false;

            if (rowLength != row.length)
                return false;
        }

        return true;
    }
    // TODO: add doc
    public static boolean isValidRectangularMatrix(int[][] toCheck) {
        Integer[][] boxed = Arrays.stream(toCheck).map(row ->
                Arrays.stream(row)
                        .boxed()
                        .toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        return isValidRectangularMatrix(boxed);
    }

    /**
     *
     */
    public static <T> String toPrettyString(T[][] toPrettify, Function<T, String> parseMatrixElementToString) {
        return StringUtils.removeLastChar(Arrays.stream(toPrettify)
                .map(row -> Arrays.stream(row)
                        .map(parseMatrixElementToString)
                        .collect(Collectors.joining()))
                .map(StringUtils::removeLastChar)
                .map(strRow -> strRow + "\n")
                .collect(Collectors.joining()));
    }

    // TODO: write doc
    public static <T> boolean haveSameSize(T[][] lhs, T[][] rhs) {
        if (!isValidRectangularMatrix(lhs) || !isValidRectangularMatrix(rhs))
            throw new IllegalArgumentException("lhs and rhs should both be valid rectangulare matrices!");

        return lhs.length == rhs.length && lhs[0].length == rhs[0].length;
    }

    // TODO: write doc
    public static <T> Position findPosOf(T[][] matrix, T toFind) {
        if (!isValidRectangularMatrix(matrix))
            throw new IllegalArgumentException("need a rectangular matrix");

        Position res = new Position(0, 0);
        for (int y = 0; y < matrix.length; ++y) {
            for (int x = 0; x < matrix[0].length; ++x) {
                if (matrix[y][x].equals(toFind))
                    res = new Position(x, y);
            }
        }

        return res;
    }


}
