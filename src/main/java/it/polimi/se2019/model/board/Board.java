package it.polimi.se2019.model.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.JsonAdapter;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.serialization.CustomFieldNamingStrategy;
import it.polimi.se2019.model.board.serialization.CustomTilesDeserializer;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    // GSON used to deal with serialization/deserialization
    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Tile.class, "type")
                .registerSubtype(NormalTile.class, "normal")
                .registerSubtype(SpawnTile.class, "spawn"))
            .setFieldNamingStrategy(new CustomFieldNamingStrategy())
            .create();

    // width and height
    int mWidth;
    int mHeight;

    // all the board tiles
    @JsonAdapter(CustomTilesDeserializer.class)
    ArrayList<Tile> mTiles;

    // trivial getters
    public List<Tile> getTiles() {
        return mTiles;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getSize() {
        return getWidth() * getHeight();
    }

    /**
     * Default constructor for empty board
     */
    public Board() {
        mWidth = 0;
        mTiles = new ArrayList<>(0);
    }

    /**
     * Constructor that fills board with default-constructed normal tiles
     * @param width width of constructed board
     * @param height height og constructed board
     */
    public Board(int width, int height) {
        mWidth = width;
        mHeight = height;
        mTiles = IntStream.range(0, width * height)
                .mapToObj(i -> new NormalTile())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Constructs a board parsing a json string
     * @param toParse the json string to parse
     * @return the constructed board object
     */
    public static Board fromJson(String toParse) {
        return GSON.fromJson(toParse, Board.class);
    }

    /**
     * Serializes board into a json string and returns it
     * @return the serialized board in the form of a json string
     */
    public String toJson() {
        return GSON.toJson(this);
    }

    /**
     *
     * @return string representation of the board
     */
    @Override
    public String toString() {
        return toJson();
    }

    /**
     * Returns a default constructed board builder
     * @return the builder
     * @deprecated will be substituted fully by json deserialization
     */
    @Deprecated
    public static Builder initializer() {
        return new Builder();
    }

    /**
     *
     * @return hashcode associated to {@code this}
     */
    @Override
    public int hashCode() {
        return Objects.hash(mWidth, mHeight, mTiles);
    }

    /**
     *
     * @param other board to check equality against
     * @return true if {@code other} is equal to {@code this}
     */
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        Board casted = (Board) other;

        if (mHeight != casted.mHeight || mWidth != casted.mWidth
                || mTiles.size() != casted.mTiles.size())
            return false;

        /* TODO: find out why using ArrayList.equals does not work
           return mHeight == casted.mHeight &&
                mWidth == casted.mWidth &&
                mTiles.equals(casted.mTiles);*/
        for (int i = 0; i < mTiles.size(); i++) {
            if (!Objects.equals(mTiles.get(i), casted.mTiles.get(i)))
                return false;
        }

        return true;
    }

    /**
     * Get tiles as a rectangular matrix
     * @return a matrix representation of this board's tiles
     */
    public List<List<Tile>> getRows() {
        return IntStream.range(0, getHeight())
                .mapToObj(i -> mTiles.subList(i * getWidth(), (i + 1) * getWidth()))
                .collect(Collectors.toList());
    }

    /**
     * Returns clone of the board
     * @return a clone of the board
     */
    public Board deepCopy() {
        Board result = new Board();

        result.mWidth = mWidth;
        result.mHeight = mHeight;

        result.mTiles = mTiles.stream()
                .map(Tile::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));

        return result;
    }

    // TODO: write doc
    private int getIndexFromPosition (Position pos) {
        int requestedIndex = mWidth * pos.getY() + pos.getX();
        if (requestedIndex > getSize()) {
            //TODO: is better to use a custom exception?
            throw new IllegalArgumentException();
        }

        return requestedIndex;
    }

    // TODO: write doc
    public Tile getTileAt (Position pos) {
        if (isOutOfBounds(pos))
            throw new IndexOutOfBoundsException("Trying to reference tile outside of the board's bounds!\n" +
                    "board width: " + getWidth() + "\n" +
                    "board height: " + getHeight() + "\n" +
                    "out-of-bounds position: " + pos
            );

        return mTiles.get(getIndexFromPosition(pos));
    }

    // TODO: write doc
    public void setTileAt (Position pos, Tile toSet) {
        mTiles.set(getIndexFromPosition(pos), toSet);
    }

    // TODO: write doc
    public int getTileDistance (Position pos1, Position pos2) {
        Tile tile1 = getTileAt(pos1);
        Tile tile2 = getTileAt(pos2);

        //same room, can use manhattan distance
        if (tile1.getColor() == tile2.getColor()) {
            return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY());
        }

        //TODO: incomplete, finish it when you have time
        return -1;
    }

    /**
     * Checks if {@code pos} is out of {@code this}'s bounds
     * @param pos the checked position
     * @return true if {@code pos} is out of bounds
     */
    public boolean isOutOfBounds(Position pos) {
        return pos.getX() < 0 || pos.getY() < 0 ||
                pos.getX() >= getWidth() || pos.getY() >= getHeight();
    }

    /**
     * Checks if two positions are in the same room
     * @param lhs first position of the positions to check
     * @param rhs second position of the positions to check
     * @return true if the {@code lhs} and {@code rhs} are in the same room
     */
    public boolean areInSameRoom(Position lhs, Position rhs) {
        return getTileAt(lhs).getColor().equals(getTileAt(rhs).getColor());
    }

    /**
     * Like {@code canSee} but works only if {@code observerPos} and {@code observedPos} are in the same room
     * @param observerPos position from where {@code observedPos} is observed
     * @param observedPos position observed by {@code observerPos}
     * @return true if {@code observedPos} can be seen from {@code observerPos}
     */
    public boolean canSeeInsideRoom(Position observerPos, Position observedPos) {
        Tile observerTile = getTileAt(observerPos);
        Tile observedTile = getTileAt(observedPos);

        // there should be no observers crawling on walls...
        if (observerTile == null)
            throw new IllegalArgumentException("Assuming observer is on a wall");

        // walls cannot be seen
        if (observedTile == null)
            return false;

        // observers can see inside their room
        if (areInSameRoom(observerPos, observedPos))
            return true;

        return false;
    }

    /**
     * Checks if {@code observedPos} can be seen from {@code observerPos}, taking doors into account
     * @param observerPos position from where {@code observedPos} is observed
     * @param observedPos position observed by {@code observerPos}
     * @return true if {@code observedPos} can be seen from {@code observerPos}
     */
    public boolean canSee(Position observerPos, Position observedPos) {
        Tile observerTile = getTileAt(observerPos);

        // two positions inside the same room can see each others
        if (canSeeInsideRoom(observerPos, observedPos))
            return true;

        // observer can peek behind a door to see a target in another room
        for (Direction doorDir : observerTile.getDoorsDirections()) {
            Position newObserver = observerPos.add(doorDir.toPosition());

            if (!isOutOfBounds(newObserver) && canSeeInsideRoom(newObserver, observedPos))
                return true;
        }

        return false;

    }

    // TODO: add doc
    public Set<Position> getAllSeenBy(Position observerPos) {
        return getRangeInfo(observerPos).getVisiblePositions();
    }

    // TODO: add doc
    public Set<Position> getReachablePositions(Position observerPos, int minDist, int maxDist) {
        return getRangeInfo(observerPos).getVisitedPositions(minDist, maxDist);
    }

    /**
     * Calculated  the positions adjacent to {@code centralPos}, filtering those that are out of bounds
     * @param centralPos interested central position
     * @return List of positions that are adjacent to {@code centralPos} and are not out of bounds
     */
    public List<Position> getAdjacentPositions(Position centralPos) {
        List<Position> result = new ArrayList<>(4);

        for (int y = -1; y <= 1; ++y) {
            for (int x = -1; x <= 1; ++x) {
                if (x == 0 || y == 0) {
                    Position adjacentPos = centralPos.add(new Position(x, y));
                    if (!isOutOfBounds(adjacentPos))
                        result.add(adjacentPos);
                }
            }
        }

        return result;
    }

    /**
     * Checks if {@code from} and {@code to} are "connected", i.e. a player can freely move
     * from {@code from} to {@code to}
     * @param from where to start
     * @param to  where to go
     * @return true if a player can freely walk from {@code from} to {@code to}
     */
    public boolean areConnected(Position from, Position to) {
        Tile fromTile = getTileAt(from);
        Tile toTile   = getTileAt(to);

        // anything involving walls is not walkable...
        if (fromTile == null || toTile == null)
            return false;

        // can walk among tiles of the same color
        if (fromTile.getColor().equals(toTile.getColor()))
            return true;

        // can walk among tiles of different color if there's a door
        if ((fromTile.getDoors()[0] && toTile.getDoors()[2]) ||
                (fromTile.getDoors()[1] && toTile.getDoors()[3]) ||
                (fromTile.getDoors()[2] && toTile.getDoors()[0]) ||
                (fromTile.getDoors()[3] && toTile.getDoors()[1]))
            return true;

        // no other way
        return false;
    }

    /**
     * Helper function for {@code getRangeInfoHelper}
     */
    private RangeInfo getRangeInfoHelper(Position originalPos, Position currPos, int currDist, RangeInfo result) {
        // if already visited and not worse (less or equally distant), stop
        if (result.isVisited(currPos) && result.getDistAt(currPos) <= currDist)
            return result;

        // otherwise position is interesting; associate it with collected info and add it to results
        result.addDistAt(currPos, currDist);
        if (canSee(originalPos, currPos)) result.toggleVisibleAt(currPos); // TODO: might be faster without using canSee

        // recurse among adjacent tiles that can be reached
        getAdjacentPositions(currPos).stream()
                .filter(adjPos -> areConnected(currPos, adjPos))
                .forEach(newPos -> getRangeInfoHelper(originalPos, newPos, currDist + 1, result));

        return result;
    }

    /**
     * Returns info about the board around the specified position
     * (see RangeInfo for more info)
     * @param rangeOrigin position of origin of range
     * @return info about range surrounding {@code rangeOrigin}
     */
    public RangeInfo getRangeInfo(Position rangeOrigin) {
        return getRangeInfoHelper(rangeOrigin, rangeOrigin, 0, new RangeInfo(rangeOrigin));
    }
}
