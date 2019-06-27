package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

import java.util.List;

public class PowerUpSelectedRequest implements Request {
    private List<Integer> mIndexes;
    private PlayerColor mViewColor;

    public PowerUpSelectedRequest(List<Integer> indexes, PlayerColor viewColor) {
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
