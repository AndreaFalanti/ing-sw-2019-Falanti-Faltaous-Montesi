package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.MatrixUtils;

import java.util.*;

/**
 * Info about a range of cells around a given position
 */
public class RangeInfo {
    // info associated with interested positions
    public final Map<Position, CellInfo> mInfo = new HashMap<>(); // TODO: remove public

    // max distance
    private int mMaxDistance = 0;

    // position to which all other positions are seen relative to
    private Position mObserverPos = new Position(0);

    public RangeInfo() {}

    public RangeInfo(Position observerPos) {
        mObserverPos = observerPos;
    }

    public static RangeInfo fromMatrix(Position observerPos, Integer[][] distMatrix, Integer[][] sightMatrix) {
        RangeInfo result = new RangeInfo();

        // set observer pos
        result.setObserverPos(observerPos);

        // check if matrices are valid
        if (!MatrixUtils.isValidRectangularMatrix(distMatrix))
            throw new IllegalArgumentException("distance matrix is not a valid rectangular matrix!");
        if (!MatrixUtils.isValidRectangularMatrix(sightMatrix))
            throw new IllegalArgumentException("sight matrix is not a valid rectangular matrix!");
        if (!MatrixUtils.haveSameSize(distMatrix, sightMatrix))
            throw new IllegalArgumentException("sight matrix and distance matrix must be the same size");

        // find position of 0
        Position posOf0 = MatrixUtils.findPosOf(distMatrix, 0);

        // convert matrices into info hashmap
        for (int y = 0; y < distMatrix.length; ++y) {
            for (int x = 0; x < distMatrix[0].length; ++x) {
                int dist = distMatrix[y][x];
                boolean visible = (sightMatrix[y][x] == 1);

                // non-visible  and unreachable positions are not stored to save space
                if (dist != -1 || visible)
                    result.addInfoAt(new Position(x, y).add(observerPos).subtract(posOf0), new CellInfo(visible, dist));
            }
        }

        return result;
    }

    // trivial setters
    public void setObserverPos(Position observerPos) {
        mObserverPos = observerPos;
    }

    // TODO: add doc, remembering to specify that *observerPos* does not matter
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        RangeInfo casted = (RangeInfo) other;

        return mInfo.equals(casted.mInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mInfo);
    }

    @Override
    public String toString() {
        // get matrix representation of RangeInfo
        Integer[][] distanceMatrix = new Integer[mMaxDistance * 2 + 1][mMaxDistance * 2 + 1];
        Integer[][] sightMatrix    = new Integer[mMaxDistance * 2 + 1][mMaxDistance * 2 + 1];
        MatrixUtils.fillRectangularMatrix(distanceMatrix,  mMaxDistance * 2 + 1, mMaxDistance * 2 + 1, -1);
        MatrixUtils.fillRectangularMatrix(sightMatrix,     mMaxDistance * 2 + 1, mMaxDistance * 2 + 1, 0);

        mInfo.entrySet().stream()
                .forEach(entry -> {
                    Position absolutePos = entry.getKey();
                    Position posRelToObserver = absolutePos.subtract(mObserverPos);
                    Position showPos = posRelToObserver.add(new Position(mMaxDistance));

                    distanceMatrix[showPos.getY()][showPos.getX()] = entry.getValue().getDistance();
                    sightMatrix   [showPos.getY()][showPos.getX()] = entry.getValue().isVisible() ? 1 : 0;
                });

        // convert matrix representation into pretty string (adding the observer position)
        return mObserverPos + "\n" +
                MatrixUtils.toPrettyString(distanceMatrix, (ele, pos) -> {
                    if (ele == null || ele == -1) {
                        return "#  ";
                    } else {
                        char visibleChar = (sightMatrix[pos.getY()][pos.getX()] == 1 ? '^' : ' ');
                        return Integer.toString(ele) + visibleChar + " ";
                    }
                });
    }

    /**
     * Checks if particular position has been already visited
     * @param pos the interested position
     * @return true if {@code pos} has already been visited
     */
    public boolean isVisited(Position pos) {
        return mInfo.containsKey(pos);
    }

    // info setters/getters
    public void addInfoAt(Position at, CellInfo info) {
        if (info.getDistance() > mMaxDistance)
            mMaxDistance = info.getDistance();

        mInfo.put(at, info);
    }
    public CellInfo getInfoAt(Position at) {
        return mInfo.get(at);
    }

    // setters for distance and sight
    public void addDistAt(Position at, int dist) {
        addInfoAt(at, new CellInfo(dist));
    }
    public void toggleVisibleAt(Position at) {
        CellInfo newCellInfo = new CellInfo(!isVisibleAt(at), getDistAt(at));
        addInfoAt(at, newCellInfo);
    }

    // getters for distance and sight
    public int getDistAt(Position at) {
        CellInfo info = getInfoAt(at);

        return (info != null) ? info.getDistance() : -1;
    }
    public boolean isVisibleAt(Position at) {
        CellInfo info = getInfoAt(at);

        return (info != null) ? info.isVisible() : false;
    }
}
