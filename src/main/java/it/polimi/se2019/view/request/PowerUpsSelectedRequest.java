package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

import java.util.List;

/**
 * Contains selected powerUp indexes that player wants to use, to send to controller
 *
 * @author Andrea Falanti
 */
public class PowerUpsSelectedRequest implements Request {
    private List<Integer> mIndexes;
    private PlayerColor mViewColor;

    public PowerUpsSelectedRequest(List<Integer> indexes, PlayerColor viewColor) {
        mIndexes = indexes;
        mViewColor = viewColor;
    }

    public List<Integer> getIndexes() {
        return mIndexes;
    }

    @Override
    public PlayerColor getViewColor() {
        return mViewColor;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
