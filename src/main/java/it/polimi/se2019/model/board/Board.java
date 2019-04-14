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
        if (!(other instanceof Board))
            return false;

        Board bOther = (Board) other;

        return bOther.mHeight == mHeight &&
                bOther.mWidth == mWidth &&
                bOther.mTiles.equals(mTiles);
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

    public Tile getTileFromPosition (Position pos) { return null; }

}
