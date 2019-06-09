package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.View;

import java.util.Set;

public class TargetsSelectedRequest implements Request {
    private Set<PlayerColor> mSelectedTargets;
    private View mView;

    // trivial constructor
    public TargetsSelectedRequest(Set<PlayerColor> selectedTargets, View view) {
        mSelectedTargets = selectedTargets;
        mView = view;
    }

    // trivial getters
    public Set<PlayerColor> getSelectedTargets() {
        return mSelectedTargets;
    }

    public View getView() {
        return mView;
    }

    @Override
    public void handleMe(RequestHandler handler) {
        handler.handle(this);
    }
}
