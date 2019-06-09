package it.polimi.se2019.controller.response;

import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.ResponseHandler;


public class PickWeaponResponse implements Response {
    // if null it means player will choose from weapons in his hand
    private TileColor mSpawnColor;

    public PickWeaponResponse(TileColor spawnColor) {
        mSpawnColor = spawnColor;
    }

    public TileColor getSpawnColor() {
        return mSpawnColor;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}