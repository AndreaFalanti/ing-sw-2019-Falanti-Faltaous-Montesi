package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;
import it.polimi.se2019.controller.responses.Response;

import java.io.Serializable;

public interface Request extends Serializable {
    Response handle (RequestHandler handler);
}
