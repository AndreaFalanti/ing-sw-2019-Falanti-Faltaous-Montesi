package it.polimi.se2019.model.action.response;

import it.polimi.se2019.controller.InvalidActionResponseHandler;

/**
 * Common interface to all action error messages
 *
 * @author Andrea Falanti
 */
public interface InvalidActionResponse {
    void handle(InvalidActionResponseHandler handler);
}
