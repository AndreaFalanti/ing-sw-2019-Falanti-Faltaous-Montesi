package it.polimi.se2019.util;

import it.polimi.se2019.model.Position;

import java.util.Arrays;
import java.util.function.BiFunction;

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

    // TODO: add doc
    public static <T> void fillRectangularMatrix(T[][] toFill, int cols, int rows, T init) {
        if (!isValidRectangularMatrix(toFill))
            throw new IllegalArgumentException("toFill should be a valid rectangular matrix!");

        for (int y = 0; y < cols; y++) {
            for (int x = 0; x < rows; x++) {
                toFill[y][x] = init;
            }
        }
    }

    /**
     *
     */
    // TODO: take into account multiple digit numbers
    public static <T> String toPrettyString(T[][] toPrettify, BiFunction<T, Position, String> parseMatrixElementToString) {
        StringBuilder result = new StringBuilder();

        for (int y = 0; y < toPrettify.length; y++) {
            for (int x = 0; x < toPrettify.length; x++) {
                result.append(parseMatrixElementToString.apply(toPrettify[y][x], new Position(y, x)));
            }
            // replace last trailing space with newline
            result.setCharAt(result.length() - 1, '\n');
        }

        return result.toString();
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
