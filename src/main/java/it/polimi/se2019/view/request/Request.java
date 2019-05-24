package it.polimi.se2019.view.request;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.controller.response.Response;

public interface Request {
    Response handleMe(RequestHandler handler);
}
