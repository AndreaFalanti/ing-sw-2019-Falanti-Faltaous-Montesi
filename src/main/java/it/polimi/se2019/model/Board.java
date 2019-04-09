package it.polimi.se2019.model;

import java.util.*;


public class Board {
    private class Builder {
        private Board mToBuild = new Board();

        public Board build() {
            return mToBuild;
        }
    }

    public Builder initializer() {
        return new Builder();
    }

    public Board() { }

    private Tile[ ][ ] scheme;

    public Tile getScheme() {
        return null;
    }

    public Tile getTileFromPosition (Position pos) { return null; }

}
