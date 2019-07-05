package it.polimi.se2019.controller.response;

import it.polimi.se2019.model.Position;
import it.polimi.se2019.view.ResponseHandler;

import java.util.Set;

/**
 * Response used to notify view of a needed position selection
 *
 * @author Andrea Falanti
 */
public class PickPositionResponse implements Response {
    private Set<Position> mPositions;

    public PickPositionResponse(Set<Position> positions) {
        mPositions = positions;
    }

    public Set<Position> getPositions() {
        return mPositions;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
