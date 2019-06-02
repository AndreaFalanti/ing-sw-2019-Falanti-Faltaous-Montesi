package it.polimi.se2019.model.weapon.response;

import it.polimi.se2019.controller.response.Response;

public abstract class SelectionResponse implements Response {
    int mMinToSelect;
    int mMaxToSelect;

    protected SelectionResponse(int minToSelect, int maxToSelect) {
        mMinToSelect = minToSelect;
        mMaxToSelect = maxToSelect;
    }
}
