package it.polimi.se2019.model.weapon.response;

import it.polimi.se2019.model.PlayerColor;
import it.polimi.se2019.view.ResponseHandler;

import java.util.Objects;
import java.util.Set;

public class TargetSelectionResponse extends SelectionResponse {
    Set<PlayerColor> mToPickFrom;

    public TargetSelectionResponse(int minToSelect, int maxToSelect, Set<PlayerColor> toPickFrom) {
        super(minToSelect, maxToSelect);

        mToPickFrom = toPickFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        TargetSelectionResponse that = (TargetSelectionResponse) o;
        return Objects.equals(mToPickFrom, that.mToPickFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mToPickFrom);
    }

    @Override
    public String toString() {
        return "TargetSelectionResponse{" +
                "mToPickFrom=" + mToPickFrom +
                '}';
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
