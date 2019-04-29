package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.util.MatrixUtils;

import java.util.*;

/**
 * Info about a range of cells around a given position
 */
public class RangeInfo {
    // info associated with interested positions
    private final Map<Position, CellInfo> mInfo = new HashMap<>();

    // max distance
    private int mMaxDistance = 0;

    // position to which all other positions are seen relative to
    private Position mObserverPos = new Position(0);

    public RangeInfo() {}

    public RangeInfo(Position observerPos) {
        mObserverPos = observerPos;
    }

    public static RangeInfo fromMatrix(Position observerPos, Integer[][] distMatrix, Boolean[][] sightMatrix) {
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
                boolean visible = sightMatrix[y][x];

                // non-visible  and unreachable positions are not stored to save space
                if (dist != -1 || !visible)
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
    // TODO: standardize this to match other equals methods (or vice versa)
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
        mInfo.entrySet().stream()
                .forEach(entry -> {
                    Position realPos = entry.getKey();
                    Position posRelativeToObserver = realPos.subtract(mObserverPos);
                    Position showPos = posRelativeToObserver.add(new Position(mMaxDistance));

                    distanceMatrix[showPos.getY()][showPos.getX()] = entry.getValue().getDistance();
                    sightMatrix   [showPos.getY()][showPos.getX()] = entry.getValue().isVisible() ? 1 : 0;
                });

        // convert matrix representation into pretty string (adding the observer position)
        return mObserverPos + "\n" +
                MatrixUtils.toPrettyString(distanceMatrix, ele -> {
                    if (ele == null) {
                        return "# ";
                    } else {
                        return ele + " ";
                    }
                }) + "\n" +
                MatrixUtils.toPrettyString(sightMatrix, ele -> ele.toString()) + "\n";
    }

    /**
     * Checks if particular position has been already visited
     * @param pos the interested position
     * @return true if {@code pos} has already been visited
     */
    public boolean isVisited(Position pos) {
        return mInfo.containsKey(pos);
    }

    // TODO: add doc
    public void addInfoAt(Position at, CellInfo info) {
        if (info.getDistance() > mMaxDistance)
            mMaxDistance = info.getDistance();

        mInfo.put(at, info);
    }

    // TODO: add doc
    public void addDistAt(Position at, int dist) {
        addInfoAt(at, new CellInfo(dist));
    }

    // TODO: add doc
    public int getDistAt(Position at) {
        return mInfo.containsKey(at) ?
                mInfo.get(at).getDistance() :
                -1;
    }

    // TODO: add doc
    public boolean isVisibleAt(Position at) {
        return mInfo.containsKey(at) ?
                mInfo.get(at).isVisible() :
                false;
    }
}
