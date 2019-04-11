package it.polimi.se2019.model.board;

import it.polimi.se2019.model.Position;

import java.util.ArrayList;

public class Board {
    /**
     * Builder for initializing Board objects
     */
    private static class Builder {
        private Board mToBuild = new Board();

        public Board build() {
            return mToBuild;
        }
    }

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
