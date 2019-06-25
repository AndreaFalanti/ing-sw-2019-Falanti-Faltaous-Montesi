package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

import java.util.Optional;

public class PowerUpSelectedRequest implements Request {
    private Integer mIndex;
    private PlayerColor mViewColor;

    public PowerUpSelectedRequest(Integer index, PlayerColor viewColor) {
        mIndex = index;
        mViewColor = viewColor;
    }

    public Optional<Integer> getIndex() {
        return Optional.ofNullable(mIndex);
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
