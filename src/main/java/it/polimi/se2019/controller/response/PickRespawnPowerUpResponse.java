package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

/**
 * Response used to notify view of a needed powerUp selection, to discard for respawning purposes
 *
 * @author Andrea Falanti
 */
public class PickRespawnPowerUpResponse implements Response {
    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
