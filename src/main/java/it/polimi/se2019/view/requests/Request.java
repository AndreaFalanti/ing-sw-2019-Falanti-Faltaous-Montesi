package it.polimi.se2019.view.requests;

import it.polimi.se2019.controller.RequestHandler;

import java.io.Serializable;

public interface Request extends Serializable {
    void handle (RequestHandler handler);
}
