package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.view.View;

public interface Request {
    void handleMe(RequestHandler handler);
    View getView();
}
