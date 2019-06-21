package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

import java.util.Optional;
import java.util.OptionalInt;

public class PowerUpSelectedRequest implements Request {
    private Integer mIndex;
    private View mView;

    public PowerUpSelectedRequest(Integer index, View view) {
        mIndex = index;
        mView = view;
    }

    public Optional<Integer> getIndex() {
        return Optional.ofNullable(mIndex);
    }

    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
