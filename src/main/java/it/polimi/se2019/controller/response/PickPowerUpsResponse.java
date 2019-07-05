package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

import java.util.List;

/**
 * Response used to notify view of a needed powerUp selection
 *
 * @author Andrea Falanti
 */
public class PickPowerUpsResponse implements Response {
    private List<Integer> mIndexes;

    public PickPowerUpsResponse(List<Integer> indexes) {
        mIndexes = indexes;
    }

    public List<Integer> getIndexes() {
        return mIndexes;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
