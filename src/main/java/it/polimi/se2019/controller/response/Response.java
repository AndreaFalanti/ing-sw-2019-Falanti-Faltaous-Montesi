package it.polimi.se2019.controller.response;

import it.polimi.se2019.view.ResponseHandler;

/**
 * Common interface to all response messages, used to transfer data from controller to view
 *
 * @author Andrea Falanti
 */
public interface Response {
    void handleMe(ResponseHandler handler);
}