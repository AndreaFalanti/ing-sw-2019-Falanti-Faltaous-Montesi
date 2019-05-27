package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;

public interface Request {
    void handleMe(RequestHandler handler);
}
