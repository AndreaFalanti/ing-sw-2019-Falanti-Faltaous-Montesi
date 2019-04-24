package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.MatrixUtils;
import it.polimi.se2019.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * Info about a range of cells around a given position
 */
public class RangeInfo {
    // distances from origin associated with interested positions
    // TODO: remove public
    public final HashMap<Position, Integer> mDistances = new HashMap<>();

    // max distance
    private int mMaxDistance = 0;

    // position to which all other positions are seen relative to
    private Position mObserverPos = new Position(0);

    public RangeInfo() {}

    public RangeInfo(Position observerPos) {
        mObserverPos = observerPos;
    }

    public static RangeInfo fromMatrix(Position observerPos, int[][] distMatrix) {
        RangeInfo result = new RangeInfo();

        // set observer pos
        result.setObserverPos(observerPos);

        // check if matrix is valid
        if (!MatrixUtils.isValidRectangularMatrix(distMatrix))
            throw new IllegalArgumentException("Called RangeInfo.fromMatrix with invalid rectangular matrix!");

        // find position of 0
        Position posOf0 = new Position(0);
        for (int y = 0; y < distMatrix.length; ++y) {
            for (int x = 0; x < distMatrix[0].length; ++x) {
                if (distMatrix[y][x] == 0)
                    posOf0 = new Position(x, y);
            }
        }

        // convert matrix into distances hashmap
        for (int y = 0; y < distMatrix.length; ++y) {
            for (int x = 0; x < distMatrix[0].length; ++x) {
                int dist = distMatrix[y][x];
                if (dist != -1)
                    result.addDistAt(new Position(x, y).add(observerPos).subtract(posOf0), dist);
            }
        }

        return result;
    }

    // trivial setters
    public void setObserverPos(Position observerPos) {
        mObserverPos = observerPos;
    }

    // TODO: add doc, remembering to specify that *observerPos* does not matter
    // TODO: standardize this to match other equals methods (or vice versa)
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        RangeInfo casted = (RangeInfo) other;

        return mDistances.equals(casted.mDistances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mDistances);
    }

    @Override
    public String toString() {
        // get matrix representation of RangeInfo
        Integer[][] matrixResult = new Integer[mMaxDistance * 2 + 1][mMaxDistance * 2 + 1];
        mDistances.entrySet().stream()
                .forEach(entry -> {
                    Position realPos = entry.getKey();
                    Position posRelativeToObserver = realPos.subtract(mObserverPos);
                    Position showPos = posRelativeToObserver.add(new Position(mMaxDistance));

                    matrixResult[showPos.getY()][showPos.getX()] = entry.getValue();
                });

        // convert matrix representation into pretty string (adding the observer position)
        return mObserverPos + "\n" +
                MatrixUtils.toPrettyString(matrixResult, ele -> {
                    if (ele == null) {
                        return "# ";
                    } else {
                        return ele + " ";
                    }
                });
    }

    /**
     * Checks if particular has been already visited
     * @param pos the interested position
     * @return true if {@code pos} has already been visited
     */
    public boolean isVisited(Position pos) {
        return mDistances.containsKey(pos);
    }

    /**
     * Adds given
     * @param at
     * @param dist
     */
    public void addDistAt(Position at, int dist) {
        if (dist > mMaxDistance)
            mMaxDistance = dist;

        mDistances.put(at, dist);
    }

    // TODO: add doc
    public OptionalInt getDistAt(Position at) {
        return mDistances.containsKey(at) ?
                OptionalInt.of(mDistances.get(at)) :
                OptionalInt.empty();
    }
}
