package it.polimi.se2019.model.weapon.response;

public class SelectionResponse implements WeaponResponse {
    int mMinToSelect;
    int mMaxToSelect;

    protected SelectionResponse(int minToSelect, int maxToSelect) {
        mMinToSelect = minToSelect;
        mMaxToSelect = maxToSelect;
    }
}
