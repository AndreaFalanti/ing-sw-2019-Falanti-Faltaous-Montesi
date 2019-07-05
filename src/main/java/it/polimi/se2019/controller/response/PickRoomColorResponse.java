package it.polimi.se2019.controller.response;

import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.ResponseHandler;

import java.util.Set;

/**
 * Response used to notify view of a needed room selection
 *
 * @author Andrea Falanti
 */
public class PickRoomColorResponse implements Response {
    private Set<TileColor> mTileColors;

    public PickRoomColorResponse(Set<TileColor> tileColors) {
        mTileColors = tileColors;
    }

    public Set<TileColor> getTileColors() {
        return mTileColors;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
