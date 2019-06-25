package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;

public class TargetsSelectedRequest implements Request {
    private Set<PlayerColor> mSelectedTargets;
    private PlayerColor mViewColor;

    // trivial constructor
    public TargetsSelectedRequest(Set<PlayerColor> selectedTargets, PlayerColor viewColor) {
        mSelectedTargets = selectedTargets;
        mViewColor = viewColor;
    }

    // trivial getters
    public Set<PlayerColor> getSelectedTargets() {
        return mSelectedTargets;
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
