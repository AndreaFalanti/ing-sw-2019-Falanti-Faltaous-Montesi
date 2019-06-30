package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.model.PlayerColor;

public interface Request {
    PlayerColor getViewColor();
    void handleMe(RequestHandler handler);
}
