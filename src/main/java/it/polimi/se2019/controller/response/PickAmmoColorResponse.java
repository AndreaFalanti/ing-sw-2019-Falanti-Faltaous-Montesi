package it.polimi.se2019.controller.response;

import it.polimi.se2019.model.board.TileColor;
import it.polimi.se2019.view.ResponseHandler;

import java.util.Set;

/**
 * Response used to notify view of a needed ammo color selection
 *
 * @author Andrea Falanti
 */
public class PickAmmoColorResponse implements Response {
    private Set<TileColor> mAmmoColors;

    public PickAmmoColorResponse(Set<TileColor> ammoColors) {
        mAmmoColors = ammoColors;
    }

    public Set<TileColor> getAmmoColors() {
        return mAmmoColors;
    }

    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
