package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

/**
 * Response used to notify view of a needed direction selection
 *
 * @author Andrea Falanti
 */
public class PickDirectionResponse implements Response {
    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
