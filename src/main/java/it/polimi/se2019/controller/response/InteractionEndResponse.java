package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

/**
 * Response used to notify view of the end of the game
 *
 * @author Andrea Falanti
 */
public class InteractionEndResponse implements Response {
    @Override
    public void handleMe(ResponseHandler handler) {
        handler.handle(this);
    }
}
