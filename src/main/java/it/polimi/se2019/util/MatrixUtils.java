package it.polimi.se2019.util;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MatrixUtils {
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
}
