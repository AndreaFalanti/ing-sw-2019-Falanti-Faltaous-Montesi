package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.board.TileColor;

public class RoomSelectedRequest implements Request {
    private TileColor mColor;
    private PlayerColor mViewColor;

    public RoomSelectedRequest(TileColor color, PlayerColor viewColor) {
        mColor = color;
        mViewColor = viewColor;
    }

    public TileColor getColor() {
        return mColor;
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
