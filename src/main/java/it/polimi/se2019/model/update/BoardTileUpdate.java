package it.polimi.se2019.model.update;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.model.board.Tile;

/**
 * Update message with a board's tile info, sent to views
 *
 * @author Andrea Falanti
 */
public class BoardTileUpdate implements Update {
    private Tile mTile;

    public BoardTileUpdate(Tile tile) {
        mTile = tile;
    }

    public Tile getTile() {
        return mTile;
    }

    public Position getTilePos() {
        return mTile.getPosition();
    }

    @Override
    public void handleMe(UpdateHandler handler) {
        handler.handle(this);
    }
}
