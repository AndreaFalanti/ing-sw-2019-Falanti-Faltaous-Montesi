package it.polimi.se2019.model.weapon.response;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.model.weapon.Selection;
import it.polimi.se2019.view.ResponseHandler;

public class TargetSelectionResponse extends SelectionResponse {
    Selection<PlayerColor> mToPickFrom;

    public TargetSelectionResponse(int minToSelect, int maxToSelect, Selection<PlayerColor> toPickFrom) {
        super(minToSelect, maxToSelect);

        mToPickFrom = toPickFrom;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
