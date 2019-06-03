package it.polimi.se2019.model.update;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Tile;

public class BoardTileUpdate implements Update {
    private Tile mTile;
    private Position mTilePos;

    public BoardTileUpdate(Tile tile, Position tilePos) {
        mTile = tile;
        mTilePos = tilePos;
    }

    public Tile getTile() {
        return mTile;
    }

    public Position getTilePos() {
        return mTilePos;
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
