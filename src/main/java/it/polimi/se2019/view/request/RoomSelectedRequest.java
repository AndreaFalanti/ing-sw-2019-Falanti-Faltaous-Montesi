package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.View;

public class RoomSelectedRequest implements Request {
    private TileColor mColor;
    private View mView;

    public RoomSelectedRequest(TileColor color, View view) {
        mColor = color;
        mView = view;
    }

    public TileColor getColor() {
        return mColor;
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
