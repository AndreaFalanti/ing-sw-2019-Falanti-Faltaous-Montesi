package it.polimi.se2019.view;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.update.Update;
import it.polimi.se2019.model.update.UpdateHandler;
import it.polimi.se2019.util.Observable;
import it.polimi.se2019.util.Observer;
import it.polimi.se2019.view.request.Request;

public abstract class View extends Observable<Request> implements Observer<Update>, ViewInterface {
    // fields
    protected PlayerColor mOwnerColor;
    protected UpdateHandler mUpdateHandler;

    // constructors
    public View(UpdateHandler updateHandler) {
        mUpdateHandler = updateHandler;
    }

    public View(PlayerColor ownerColor) {
        mOwnerColor = ownerColor;
    }

    public View(PlayerColor ownerColor, UpdateHandler updateHandler) {
        mOwnerColor = ownerColor;
        mUpdateHandler = updateHandler;
    }

    public UpdateHandler getUpdateHandler() {
        return mUpdateHandler;
    }

    public PlayerColor getOwnerColor() {
        return mOwnerColor;
    }

    public void setOwnerColor(PlayerColor ownerColor) {
        mOwnerColor = ownerColor;
    }

    @Override
    public void update(Update update) {
        update.handleMe(mUpdateHandler);
    }
}
