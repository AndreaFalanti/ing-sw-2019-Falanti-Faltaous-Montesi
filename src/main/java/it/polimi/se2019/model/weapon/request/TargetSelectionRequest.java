package it.polimi.se2019.model.weapon.request;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Selection;

public class TargetSelectionRequest extends SelectionRequest {
    Selection<PlayerColor> mToPickFrom;

    public TargetSelectionRequest(int minToSelect, int maxToSelect, Selection<PlayerColor> toPickFrom) {
        super(minToSelect, maxToSelect);

        mToPickFrom = toPickFrom;
    }
}
