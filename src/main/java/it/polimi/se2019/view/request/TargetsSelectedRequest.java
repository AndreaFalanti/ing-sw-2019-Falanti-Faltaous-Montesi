package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

import java.util.Set;

public class TargetsSelectedRequest implements Request {
    private Set<PlayerColor> mSelectedTargets;

    // trivial constructor
    public TargetsSelectedRequest(Set<PlayerColor> selectedTargets) {
        mSelectedTargets = selectedTargets;
    }

    // trivial getters
    public Set<PlayerColor> getSelectedTargets() {
        return mSelectedTargets;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
