package it.polimi.se2019.model.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.serialization.CustomFieldNamingStrategy;
import it.polimi.se2019.util.gson.extras.typeadapters.RuntimeTypeAdapterFactory;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(RuntimeTypeAdapterFactory.of(Tile.class, "type")
                .registerSubtype(NormalTile.class, "normal")
                .registerSubtype(SpawnTile.class, "spawn"))
            .setFieldNamingStrategy(new CustomFieldNamingStrategy())
            .create();

    private int mWidth;
    private int mHeight;
    private ArrayList<Tile> mTiles;

    /**
     * Default constructor for empty board
     */
    public Board() {
        mWidth = 0;
        mTiles = new ArrayList<>(0);
    }

    /**
     * Default helper constructor that fills board with default-constructed
     * normal tiles
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

    @Override
    public String toString() {
        return toJson();
    }

    public static Builder initializer() {
        return new Builder();
    }

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

        // TODO: find out why using ArrayList.equals does not work
        /* return mHeight == casted.mHeight &&
                mWidth == casted.mWidth &&
                mTiles.equals(casted.mTiles);*/
        for (int i = 0; i < mTiles.size(); i++) {
            if (!mTiles.get(i).equals(casted.mTiles.get(i)))
                return false;
        }

        return true;
    }

    public Board deepCopy() {
        Board result = new Board();

        result.mWidth = mWidth;
        result.mHeight = mHeight;

        result.mTiles = mTiles.stream()
                .map(Tile::deepCopy)
                .collect(Collectors.toCollection(ArrayList::new));

        return result;
    }

    public ArrayList<Tile> getTiles() {
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
    };

    private int getIndexFromPosition (Position pos) {
        int requestedIndex = mWidth * pos.getY() + pos.getX();
        if (requestedIndex > getSize()) {
            //TODO: is better to use a custom exception?
            throw new IllegalArgumentException();
        }

        return requestedIndex;
    }

    public boolean isValidTilePosition (Position pos) {
        // X >= 0 && Y >= 0 is checked in Position constructor
        return pos.getX() < mWidth && pos.getY() < mHeight;
    }

    public Tile getTileAt (Position pos) {
        return isValidTilePosition(pos) ? mTiles.get(getIndexFromPosition(pos)) : null;
    }

    public Tile getTileAt (int x, int y) {
        Position pos = new Position(x, y);
        return getTileAt(pos);
    }

    public void setTileAt (Position pos, Tile toSet) {
        mTiles.set(getIndexFromPosition(pos), toSet);
    }

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

    public boolean hasVisibility (Position observerPos, Position targetPos) {
        return false;
    }

}
