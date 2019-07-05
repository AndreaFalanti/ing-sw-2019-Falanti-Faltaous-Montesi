package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

/**
 * Basic interface for all requests
 *
 * @author Andrea Falanti
 */
public interface Request {
    PlayerColor getViewColor();
    void handleMe(RequestHandler handler);
}
