package it.polimi.se2019.controller.response;

import it.polimi.se2019.controller.weapon.Effect;
import it.polimi.se2019.view.ResponseHandler;

import java.util.Set;
import java.util.SortedMap;

public class PickEffectsResponse implements Response {
    private SortedMap<Integer, Set<Effect>> mPriorityMap;
    private Set<Effect> mPossibleEffects;

    public PickEffectsResponse(SortedMap<Integer, Set<Effect>> priorityMap, Set<Effect> possibleEffects) {
        mPriorityMap = priorityMap;
        mPossibleEffects = possibleEffects;
    }

    public SortedMap<Integer, Set<Effect>> getPriorityMap() {
        return mPriorityMap;
    }

    public Set<Effect> getPossibleEffects() {
        return mPossibleEffects;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
