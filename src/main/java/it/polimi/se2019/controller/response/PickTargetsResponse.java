package it.polimi.se2019.controller.response;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.ResponseHandler;

import java.util.Set;

/**
 * Response used to notify view of a needed target selection
 *
 * @author Andrea Falanti
 */
public class PickTargetsResponse implements Response {
    private int mMinTargets;
    private int mMaxTargets;
    private Set<PlayerColor> mTargets;

    public PickTargetsResponse(int minTargets, int maxTargets, Set<PlayerColor> targets) {
        mMinTargets = minTargets;
        mMaxTargets = maxTargets;
        mTargets = targets;
    }

    public int getMinTargets() {
        return mMinTargets;
    }

    public int getMaxTargets() {
        return mMaxTargets;
    }

    public Set<PlayerColor> getTargets() {
        return mTargets;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
