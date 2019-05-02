package it.polimi.se2019.model.weapon.request;

import it.polimi.se2019.model.PlayerColor;

import java.util.Set;

public class TargetSelectionRequest extends SelectionRequest {
    Set<PlayerColor> mToPickFrom;

    public TargetSelectionRequest(int minToSelect, int maxToSelect, Set<PlayerColor> toPickFrom) {
        super(minToSelect, maxToSelect);

        mToPickFrom = toPickFrom;
    }
}
