package it.polimi.se2019.model.board;

/**
 * Info about a range of cells around a given position
 */
public class RangeInfo {
    // matrix of distances from origin
    private int[][] mDistances;

    // origin (where 0 distance is located inside the mDistances matrix)
    // TODO: find out if this is needed
    // private int mOrigin;

    public static RangeInfo fromMatrix(int[][] distMatrix) {
        RangeInfo result = new RangeInfo();

        result.mDistances = distMatrix;

        return result;
    }
}
