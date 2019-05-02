package it.polimi.se2019.model.weapon.request;

public class SelectionRequest {
    int mMinToSelect;
    int mMaxToSelect;

    protected SelectionRequest(int minToSelect, int maxToSelect) {
        mMinToSelect = minToSelect;
        mMaxToSelect = maxToSelect;
    }
}
