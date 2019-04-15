package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Board {

    // fields
    private ArrayList<Tile> mTiles;
    private int mWidth;
    private int mHeight;

    /**
     * Constructs a board parsing a json string
     * @param toParse the json string to parse
     * @return the constructed board object
     */
    public static Board fromJson(String toParse) {
        // TODO: use the exclusion + custom deserializer method
        return null;
    }

    public static Builder initializer() {
        return new Builder();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other.getClass() != other.getClass())
            return false;

        Board casted = (Board) other;

        return mHeight == casted.mHeight &&
                mWidth == casted.mWidth &&
                mTiles.equals(casted.mTiles);
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

    public Tile getTileFromPosition (Position pos) {
        return mTiles.get(getIndexFromPosition(pos));
    }

    public int getTileDistance (Position pos1, Position pos2) {
        Tile tile1 = getTileFromPosition(pos1);
        Tile tile2 = getTileFromPosition(pos2);

        //same room, can use manhattan distance
        if (tile1.getColor() == tile2.getColor()) {
            return Math.abs(pos1.getX() - pos2.getX()) + Math.abs(pos1.getY() - pos2.getY());
        }

        //TODO: incomplete, finish it when you have time
        return -1;
    }

    private Tile getUpperTile (Position pos) {
        if (pos.getY() == 0) {
            return null;
        }
        return mTiles.get(getIndexFromPosition(pos) - mWidth);
    }

}
